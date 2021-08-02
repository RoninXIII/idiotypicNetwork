/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
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
public class Bcell {

	protected String type = ""; // check whether the cell is alive or not
	private String[] typeList = { "naive", "activated" };
	private ContinuousSpace<Object> space;
	protected int id;
	protected int antigenId;
	public static int agentsCardinality = getCardinality();
	public static int agentsToCheck = agentsCardinality;

	/*
	 * The Cell will move about the ContinuousSpace and we will simply round the
	 * ContinuousSpace location to determine the corresponding Grid location
	 */
	private Grid<Object> grid;
	public static ArrayList<Object> cellsToRemove = new ArrayList<Object>();
	public static ArrayList<GridCell<Bcell>> cellsToAdd = new ArrayList<GridCell<Bcell>>();

	public Bcell(Grid<Object> grid, String type, int id) {
		super();

		this.grid = grid;
		this.type = type;
		this.id = id;
	}

	private static int getCardinality() {

		Parameters params = RunEnvironment.getInstance().getParameters();
		int cellCount = params.getInteger("cell_count");
		return cellCount;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		GridPoint freeCell = null;
		// GridPoint freeCellForClone = null;

		if (this.lookForAntigen(gridCells) && this.type == "activated") {

			Antigen antigen = (Antigen) this.getAntigen(gridCells);

			if (this.antigenId == antigen.id) {
				// this.cloneCell(gridCells);
				nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1);

				// import repast . simphony . query . space . grid . GridCell
				gridCells = nghCreator.getNeighborhood(false);

				SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

				this.releaseAntiBodies(gridCells);
			}

		} else if (this.lookForApc(gridCells)) {

			Antigen antigen = this.getAntigenFromApc(gridCells);

			if (antigen != null) {
				this.antigenId = antigen.id;
				this.type = typeList[1];
			}

		} else if (this.lookForTcell(gridCells)) {
			Tcell tcell = this.getTcell(gridCells);
			
			if (tcell.type == "helper") {
				this.cloneCell(gridCells);
				tcell.bcells++;
			}else {
				
				
			}
			
			

		} else {

			for (GridCell<Object> gridCell : gridCells) {

				if (gridCell.size() == 0) {
					freeCell = gridCell.getPoint();

				}

			}
		}

		if (freeCell != null) {
			// this.type = typeList[1];
			this.moveTowards(freeCell);

		}

	}

	private Tcell getTcell(List<GridCell<Object>> gridCells) {
		
		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Tcell")) {
				List<Object> list = (List<Object>) gridCell.items();

				Tcell tcell = (Tcell) list.get(0);

				return tcell;
			}
		}

		return null;
	}

	private Antigen getAntigen(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Antigen") && gridCell.size() != 0
					&& !gridCell.items().toString().contains("AntigenPresentingCell")) {
				List<Object> list = (List<Object>) gridCell.items();

				Antigen antigen = (Antigen) list.get(0);

				return antigen;
			}
		}

		return null;
	}

	private Antigen getAntigenFromApc(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("AntigenPresentingCell")) {
				List<Object> list = (List<Object>) gridCell.items();

				AntigenPresentingCell apc = (AntigenPresentingCell) list.get(0);

				if (apc.antigensToPresent.size() != 0) {
					Antigen antigen = apc.antigensToPresent.get(0);
					apc.antigensToPresent.remove(0);
					return antigen;
				} else
					return null;
			}
		}

		return null;
	}

	public boolean lookForAntigen(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Antigen")
					&& !gridCell.items().toString().contains("PresentingCell")) {
				return true;
			}
		}

		return false;
	}

	public boolean lookForApc(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("AntigenPresentingCell")) {

				return true;
			}
		}

		return false;

	}

	public boolean lookForTcell(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Tcell")) {

				return true;
			}
		}

		return false;

	}

	public boolean cloneCell(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() == 0) {
				Context<Object> context = ContextUtils.getContext(this);
				GridPoint pt = gridCell.getPoint();
				Bcell newCell = new Bcell(grid, "naive", this.id);
				context.add(newCell);
				grid.moveTo(newCell, pt.getX(), pt.getY());
				return true;
			}
		}

		return false;

	}

	public void moveTowards(GridPoint pt) {

		if (!pt.equals(grid.getLocation(this))) {

			grid.moveTo(this, pt.getX(), pt.getY());

		}

	}

	public boolean isAlreadyPresent(GridCell<Bcell> gridCell) {

		for (GridCell<Bcell> gridCell2 : cellsToAdd) {

			if (gridCell2.getPoint().equals(gridCell.getPoint())) {

				return true;
			}
		}

		return false;

	}
	/*
	 * 
	 * /*private void removeCell() {
	 * 
	 * for (Object obj : cellsToRemove) { Context<Object> context =
	 * ContextUtils.getContext(obj); context.remove(obj);
	 * 
	 * }
	 * 
	 * agentsCardinality = agentsCardinality - cellsToRemove.size();
	 * 
	 * if (agentsCardinality == 0) { agentsCardinality = getCardinality(); }
	 * 
	 * cellsToRemove.clear();
	 * 
	 * }
	 */

	public void releaseAntiBodies(List<GridCell<Object>> gridCells) {

		Context<Object> context = ContextUtils.getContext(this);

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() == 0) {
				GridPoint pt = gridCell.getPoint();
				Antibody ab = new Antibody(grid, this.id, "killer");
				context.add(ab);
				grid.moveTo(ab, pt.getX(), pt.getY());
			}
		}

	}

	public List<GridCell<Bcell>> getNeighbors(GridCell<Bcell> cell) {

		// get the grid location of this Cell
		GridPoint pt = cell.getPoint();

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Bcell> nghCreator = new GridCellNgh<Bcell>(grid, pt, Bcell.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Bcell>> gridCells = nghCreator.getNeighborhood(true);

		return gridCells;
		/*
		 * int numberOfNeighbors = this.checkLiveness(gridCells); if(numberOfNeighbors
		 * == 3) {
		 * 
		 * 
		 * 
		 * Bcell newCell = new Bcell(grid, true); Context <Object> context =
		 * ContextUtils.getContext(cell); context .add (newCell);
		 * 
		 * grid.moveTo (newCell, pt. getX (), pt. getY ()); }
		 */

	}

}
