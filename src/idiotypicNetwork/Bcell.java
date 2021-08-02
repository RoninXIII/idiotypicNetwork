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
	private String[] typeList = {"naive","activated"};
	private ContinuousSpace<Object> space;
	protected int id;
	public static int agentsCardinality = getCardinality();
	public static int agentsToCheck = agentsCardinality;
	
	/*
	 * The Cell will move about the ContinuousSpace and we will simply round the
	 * ContinuousSpace location to determine the corresponding Grid location
	 */
	private Grid<Object> grid;
	public static ArrayList<Object> cellsToRemove = new ArrayList<Object>();
	public static ArrayList<GridCell<Bcell>> cellsToAdd = new ArrayList<GridCell<Bcell>>();

	public Bcell(Grid<Object> grid, String type,int id) {
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
		//GridPoint freeCellForClone = null;
		
		if (!this.lookForAntigen(gridCells)) {
			
		
		for (GridCell<Object> gridCell : gridCells) {
			
			
			if (gridCell.size() == 0) {
				freeCell = gridCell.getPoint();
				
			}
			
		}
		
		}else {
			
			this.cloneCell(gridCells);
			
			
		}
		
		if (freeCell != null) {
			//this.type = typeList[1];
			this.moveTowards(freeCell);
			
		}
		
	
	/*	
		int numberOfNeighbors = this.checkLiveness(gridCells);
		
		
		agentsToCheck--;

		if (numberOfNeighbors == 2 || numberOfNeighbors == 3) {

			// do nothing

		} else {

			cellsToRemove.add(this);

		}

		for (GridCell<Bcell> gridCell : gridCells) {

			List<Bcell> list = (List<Bcell>) gridCell.items();

			if (list.isEmpty()) {

				List<GridCell<Bcell>> neighborsCells = getNeighbors(gridCell);

				if (checkLiveness(neighborsCells) == 3 && !isAlreadyPresent(gridCell)) {
								
					
					cellsToAdd.add(gridCell);
				}
		

			}

		}

		if (agentsToCheck == 0) {

			
			this.addCells();
			this.removeCell();

		

			agentsToCheck = agentsCardinality;
		}*/

	}
	
	
	
	public boolean lookForAntigen(List<GridCell<Object>> gridCells){
		
		for (GridCell<Object> gridCell : gridCells) {
			
			if (gridCell.size() != 0 && gridCell.items().toString().contains("Antigen") && !gridCell.items().toString().contains("PresentingCell")) {
				return true;
			}
		}
		
		return false;
	}
	
	
	
	public boolean cloneCell(List<GridCell<Object>> gridCells) {
		
		for (GridCell<Object> gridCell : gridCells) {
			
			if (gridCell.size() == 0) {
				 Context <Object > context = ContextUtils.getContext(this);
				GridPoint pt = gridCell.getPoint();
				Bcell newCell = new Bcell(grid, "activated", this.id);
				 context .add (newCell );
				grid . moveTo ( newCell , pt. getX (), pt. getY ());
				return true;
			}
		}
		
		return false;
		
		
	}
	
	
public void moveTowards(GridPoint pt) {
		
		if (!pt.equals(grid.getLocation(this))) {
			
			/*NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(),pt.getY());
 			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
 			space.moveByVector(this, 1, angle,0);
 			myPoint = space.getLocation(this);*/
 			grid.moveTo(this, pt.getX(), pt.getY());
			//moved = true;
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
/*	private void addCells() {

		for (GridCell<Bcell> gridCell : cellsToAdd) {

			Bcell cell = new Bcell(grid, typeList[1]);
			Object obj = this;
			GridPoint pt = gridCell.getPoint();
			Context<Object> context = ContextUtils.getContext(obj);
			context.add(cell);
			grid.moveTo(cell, pt.getX(), pt.getY());
		}

		agentsCardinality = agentsCardinality + cellsToAdd.size();

		cellsToAdd.clear();

	}*/

	/*private void removeCell() {

		for (Object obj : cellsToRemove) {
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);

		}

		agentsCardinality = agentsCardinality - cellsToRemove.size();

		if (agentsCardinality == 0) {
			agentsCardinality = getCardinality();
		}

		cellsToRemove.clear();

	}*/

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

	public int checkLiveness(List<GridCell<Bcell>> neighbCells) {

		int countCells = 0;
		int num = 0;

		for (GridCell<Bcell> gridCell : neighbCells) {

			num = gridCell.size();

			if (num == 1) {
				countCells++;
			}

		}

		return countCells;

	}

}
