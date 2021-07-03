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
public class LifeCell {

	protected boolean isAlive = false; // check whether the cell is alive or not

	private ContinuousSpace<Object> space;
	public static int agentsCardinality = getCardinality();
	public static int agentsToCheck = agentsCardinality;
	/*
	 * The Cell will move about the ContinuousSpace and we will simply round the
	 * ContinuousSpace location to determine the corresponding Grid location
	 */
	private Grid<Object> grid;
	public static ArrayList<Object> cellsToRemove = new ArrayList<Object>();
	public static ArrayList<GridCell<LifeCell>> cellsToAdd = new ArrayList<GridCell<LifeCell>>();

	public LifeCell(Grid<Object> grid, boolean isAlive) {
		super();

		this.grid = grid;
		this.isAlive = isAlive;
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
		GridCellNgh<LifeCell> nghCreator = new GridCellNgh<LifeCell>(grid, pt, LifeCell.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<LifeCell>> gridCells = nghCreator.getNeighborhood(false);

		int numberOfNeighbors = this.checkLiveness(gridCells);
		agentsToCheck--;

		if (numberOfNeighbors == 2 || numberOfNeighbors == 3) {

			// do nothing

		} else {

			cellsToRemove.add(this);

		}

		for (GridCell<LifeCell> gridCell : gridCells) {

			List<LifeCell> list = (List<LifeCell>) gridCell.items();

			if (list.isEmpty()) {

				List<GridCell<LifeCell>> neighborsCells = getNeighbors(gridCell);

				if (checkLiveness(neighborsCells) == 3 && !isAlreadyPresent(gridCell)) {
								
					
					cellsToAdd.add(gridCell);
				}
		

			}

		}

		if (agentsToCheck == 0) {

			
			this.addCells();
			this.removeCell();

		

			agentsToCheck = agentsCardinality;
		}

	}

	
	public boolean isAlreadyPresent(GridCell<LifeCell> gridCell) {
		
	
		
		for (GridCell<LifeCell> gridCell2 : cellsToAdd) {
			
			if (gridCell2.getPoint().equals(gridCell.getPoint())) {
				
				return true;
			}
		}
		
		return false;
		
		
		
	}
	private void addCells() {

		for (GridCell<LifeCell> gridCell : cellsToAdd) {

			LifeCell cell = new LifeCell(grid, true);
			Object obj = this;
			GridPoint pt = gridCell.getPoint();
			Context<Object> context = ContextUtils.getContext(obj);
			context.add(cell);
			grid.moveTo(cell, pt.getX(), pt.getY());
		}

		agentsCardinality = agentsCardinality + cellsToAdd.size();

		cellsToAdd.clear();

	}

	private void removeCell() {

		for (Object obj : cellsToRemove) {
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);

		}

		agentsCardinality = agentsCardinality - cellsToRemove.size();

		if (agentsCardinality == 0) {
			agentsCardinality = getCardinality();
		}

		cellsToRemove.clear();

	}

	public List<GridCell<LifeCell>> getNeighbors(GridCell<LifeCell> cell) {

		// get the grid location of this Cell
		GridPoint pt = cell.getPoint();

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<LifeCell> nghCreator = new GridCellNgh<LifeCell>(grid, pt, LifeCell.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<LifeCell>> gridCells = nghCreator.getNeighborhood(true);

		return gridCells;
		/*
		 * int numberOfNeighbors = this.checkLiveness(gridCells); if(numberOfNeighbors
		 * == 3) {
		 * 
		 * 
		 * 
		 * LifeCell newCell = new LifeCell(grid, true); Context <Object> context =
		 * ContextUtils.getContext(cell); context .add (newCell);
		 * 
		 * grid.moveTo (newCell, pt. getX (), pt. getY ()); }
		 */

	}

	public int checkLiveness(List<GridCell<LifeCell>> neighbCells) {

		int countCells = 0;
		int num = 0;

		for (GridCell<LifeCell> gridCell : neighbCells) {

			num = gridCell.size();

			if (num == 1) {
				countCells++;
			}

		}

		return countCells;

	}

}
