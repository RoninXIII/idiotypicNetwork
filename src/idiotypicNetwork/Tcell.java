/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bsh.This;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.relogo.ide.dynamics.NetLogoSystemDynamicsParser.intg_return;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author mario
 *
 */
public class Tcell {

	protected String type = "";
	private String[] typeList = { "helper", "suppressor", "naive", "activated" };
	protected String type2 = "";
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;

	// If the t cell is an helper it will elicit only the bcell with this id
	protected int antigenId = 0;
	// The list of all ids present in the model.
	// This is useful since the ids are assigned in a random way and so it is
	// possible that not all numbers are used
	public static List<Integer> antigensIds = new ArrayList<>();
	// The ids of the antibodies to suppress.
	// They will be put in the array only if the antigens with a certain id are
	// eliminated and if the
	// number of the antibodies has reached the threshold (it starts the suppression
	// operation
	protected List<Integer> antibodiesToSuppress = new ArrayList<>();

	public Tcell(ContinuousSpace<Object> space, Grid<Object> grid, String type, String type2,
			List<Integer> antigensIds) {
		super();

		this.grid = grid;
		this.space = space;
		this.type = type;
		this.type2 = type2;
		this.antigensIds = antigensIds;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<AntigenPresentingCell> nghCreator = new GridCellNgh<AntigenPresentingCell>(grid, pt,
				AntigenPresentingCell.class, 1, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<AntigenPresentingCell>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Antibody> antibodiesNghCreator = new GridCellNgh<Antibody>(grid, pt, Antibody.class, 1, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Antibody>> antibodiesGridCells = antibodiesNghCreator.getNeighborhood(false);

		SimUtilities.shuffle(antibodiesGridCells, RandomHelper.getUniform());

		GridPoint freeCell = null;

		// If naive t helper encounters an apc it will be activated
		if (this.type == "helper" && this.type2 == "naive" && this.lookForApc(gridCells)) {

			Antigen antigen = this.getAntigenFromApc(gridCells);

			if (antigen != null) {
				this.antigenId = antigen.id;
				this.type2 = typeList[3];
			}
			return;

		} else if (this.type == "suppressor" && this.type2 == "naive") {

			Context<Object> context = ContextUtils.getContext(this);

			// If all antigens are eliminated and the number of antibodies has reached the
			// threshold
			if (this.checkSuppressionConditions()) {
				// The t suppressor start searching for antibodies to suppress since the antigen
				// is not alive anymore
				this.type2 = this.typeList[3];
				return;
			}

		} else if (this.type == "suppressor" && this.type2 == "activated") {

			Context<Object> context = ContextUtils.getContext(this);

			GridPoint pointWithMostAntibodies = null;
			int maxCount = -1;
			for (GridCell<Antibody> cell : antibodiesGridCells) {
				if (cell.size() > maxCount) {
					pointWithMostAntibodies = cell.getPoint();
					maxCount = cell.size();
				}
			}

			if (this.checkSuppressionConditions()) {
				suppress();
			}

			moveTowards(pointWithMostAntibodies);

			return;

		}

		for (GridCell<AntigenPresentingCell> gridCell : gridCells) {

			if (gridCell.size() == 0) {
				freeCell = gridCell.getPoint();

			}

		}

		if (freeCell != null) {

			this.moveTowards(freeCell);

		}

	}

	private boolean checkSuppressionConditions() {

		Bcell bcell = new Bcell(space, grid, "", 0);
		// Number of antibodies for each antigen id
		HashMap<Integer, Integer> antibodiesHashMap = bcell.antibodiesHashMap;
		List<Integer> antibodiesToKillList = new ArrayList<>();

		// Loop through all antigens ids in the system
		for (Integer id : antigensIds) {

			if (this.getAntigensNumber(id) == 0) {
				antibodiesToKillList.add(id);

			}

			// If an id is present inside antibodyToSuppress it means that all the antigens
			// with this id are eliminated.
			// If there is this scenario and if the number of antibodies of this kind has
			// reached the threshold
			// the t cells will start the suppression of the b cells and the antibodies that
			// met the antigen with
			// the previous id.

			if (antibodiesToKillList.contains(id) && antibodiesHashMap.get(id) >= 10) {
				antibodiesToSuppress.add(id);
			}

		}

		if (antibodiesToSuppress.size() > 0) {
			return true;
		} else
			return false;

	}



	// Retrieve antigen from the just met Apc
	// When i enter this method i know for sure that i will get an Apc
	private Antigen getAntigenFromApc(List<GridCell<AntigenPresentingCell>> gridCells) {

		AntigenPresentingCell apc = null;

		// Cycle through the neighborhood
		for (GridCell<AntigenPresentingCell> gridCell : gridCells) {

			// Cycle through the elements inside the cells since
			// Every cell in the neighborhood can have multiple Apc inside
			for (Object obj : gridCell.items()) {
				if (obj instanceof AntigenPresentingCell) {
					apc = (AntigenPresentingCell) obj;
				}

			}
			// Hypothetically the apc can phagocytize more than one antigen
			// So the first one is retrieved and removed to present every time
			// different Antigens to other Apc
			if (apc != null && apc.antigensToPresent.size() != 0) {
				// int pos = RandomHelper.nextIntFromTo(0, apc.antigensToPresent.size()-1);
				Antigen antigen = apc.antigensToPresent.get(0);
				apc.antigensToPresent.remove(0);
				return antigen;
			} else
				return null;

		}

		return null;
	}

	public boolean lookForApc(List<GridCell<AntigenPresentingCell>> gridCells) {

		for (GridCell<AntigenPresentingCell> gridCell : gridCells) {

			if (gridCell.size() != 0) {

				return true;
			}
		}

		return false;

	}

	public int getAntigensNumber(int id) {

		Context<Object> context = ContextUtils.getContext(this);
		int count = 0;
		for (int i = 0; i < context.getObjects(Antigen.class).size(); i++) {
			Antigen antigen = (Antigen) context.getObjects(Antigen.class).get(i);
			if (antigen.id == id) {
				count++;
			}
		}

		return count;
	}

	public int getAntibodiesNumber(int id) {

		Context<Object> context = ContextUtils.getContext(this);
		int count = 0;
		for (int i = 0; i < context.getObjects(Antibody.class).size(); i++) {
			Antibody antibody = (Antibody) context.getObjects(Antibody.class).get(i);
			if (antibody.id == id) {
				count++;
			}
		}

		return count;
	}

	public void moveTowards(GridPoint pt) {
		double heading = 0;
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY(), pt.getZ());
			// Randomly change the current heading plus or minus 50 degrees
			double sgn = RandomHelper.nextDoubleFromTo(-0.5, 0.5); // a value between -0.5 and 0.5
			if (sgn > 0)
				heading = heading + RandomHelper.nextDoubleFromTo(0, 50);
			else
				heading = heading - RandomHelper.nextDoubleFromTo(0, 50);

			// Move the agent on the space by one unit according to its new heading
			// space.moveByVector(this, 1, Math.toRadians(heading),0,0);
			space.moveTo(this, otherPoint.getX(), otherPoint.getY(), otherPoint.getZ());
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY(), (int) myPoint.getZ());
			// moved = true;
		}

	}

	public void suppress() {

		GridPoint pt = grid.getLocation(this);

		// This list will contain all the antibodies encountered
		List<Object> antibodiesToSuppressList = new ArrayList<Object>();
		List<Object> bcellsToSuppressList = new ArrayList<Object>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY(), pt.getZ())) {
			if (obj instanceof Antibody) {
				antibodiesToSuppressList.add(obj);
			}else if (obj instanceof Bcell ) {
				bcellsToSuppressList.add(obj);
			}
		}
		
		

		for (Object object : antibodiesToSuppressList) {
			Antibody antibody = (Antibody) object;
			
			if (antibodiesToSuppress.contains(antibody.antigenId)) {
				Context<Object> context = ContextUtils.getContext(object);
				context.remove(object);
			}

		}
		
		for (Object object : bcellsToSuppressList) {
			Bcell bcell = (Bcell) object;
			
			if (antibodiesToSuppress.contains(bcell.antigenId)) {
				Context<Object> context = ContextUtils.getContext(object);
				context.remove(object);
			}

		}

	}

}
