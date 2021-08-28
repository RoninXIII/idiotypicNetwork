/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.List;

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
	protected int antigenId = 0;
	protected static int bcells = getBcells();
	protected static List<Integer> antibodiesToKill = new ArrayList<>();
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;

	public Tcell(ContinuousSpace<Object> space, Grid<Object> grid, String type, int bcells, String type2) {
		super();

		this.grid = grid;
		this.space = space;
		this.type = type;
		this.bcells = bcells;
		this.type2 = type2;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		bcells = this.getBcells();

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		GridPoint freeCell = null;

		if (this.type2 == "naive" && this.type == "helper" && this.lookForApc(gridCells)) {

			Antigen antigen = this.getAntigenFromApc(gridCells);

			if (antigen != null) {
				this.antigenId = antigen.id;
				this.type2 = typeList[3];
			}

		} else if (this.type == "suppressor") {

			int killedAntibodies = 0;
			for (GridCell<Object> gridCell : gridCells) {

				if (gridCell.size() != 0 && gridCell.items().toString().contains("Antibody")) {
					List<Antibody> antibodies = this.getAntibodies((List<Object>) gridCell.items());

					if (killedAntibodies < 2) {

						Context<Object> context = ContextUtils.getContext(this);

						context.remove(antibodies.get(0));
						antibodies.remove(0);
						killedAntibodies++;

					}

				} else if (gridCell.size() == 0) {
					freeCell = gridCell.getPoint();
					this.moveTowards(freeCell);

				}

			}

		} else {

			for (GridCell<Object> gridCell : gridCells) {

				if (gridCell.size() == 0) {
					freeCell = gridCell.getPoint();

				}

			}

			if (freeCell != null) {

				this.moveTowards(freeCell);

			}

		}

	}

	public List<Antibody> getAntibodies(List<Object> list) {
		List<Antibody> list2 = new ArrayList<>();

		for (Object obj : list) {
			if (obj instanceof Antibody) {			
				list2.add((Antibody) obj);
			}

		}
		
		return list2;

	}

	// Retrieve antigen from the just met Apc
	// When i enter this method i know for sure that i will get an Apc
	private Antigen getAntigenFromApc(List<GridCell<Object>> gridCells) {
		
		
		AntigenPresentingCell apc = null;
		
		//Cycle through the neighborhood
		for (GridCell<Object> gridCell : gridCells) {

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
				//	int pos = RandomHelper.nextIntFromTo(0, apc.antigensToPresent.size()-1);
					Antigen antigen = apc.antigensToPresent.get(0);
					apc.antigensToPresent.remove(0);
					return antigen;
				} else
					return null;
		
		}

		return null;
	}

	public boolean lookForApc(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("AntigenPresentingCell")) {

				return true;
			}
		}

		return false;

	}

	private static int getBcells() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		int cellCount = params.getInteger("cell_count");
		return cellCount;
	}

	public void moveTowards(GridPoint pt) {

		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
			// moved = true;
		}

	}

	public void suppress() {

	}

}
