/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
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
	 * The Cell will move about the ContinuousSpace and we will simply round
		the ContinuousSpace location to determine the corresponding Grid location
	 * */
	private Grid<Object> grid;
	public static ArrayList<Object> cellsToRemove = new ArrayList<Object>();
	
	public LifeCell(Grid<Object> grid,boolean isAlive) {
		super();


		this.grid = grid;
		this.isAlive = isAlive;
	}
	
	private static int getCardinality() {
		
		Parameters params = RunEnvironment.getInstance ().getParameters();
		int cellCount = params.getInteger("cell_count");
		return cellCount;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		
		
		// get the grid location of this Cell
		 GridPoint pt = grid.getLocation (this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		 GridCellNgh <LifeCell> nghCreator = new GridCellNgh <LifeCell>( grid , pt , LifeCell.class , 1, 1);
		 
		// import repast . simphony . query . space . grid . GridCell
		 List< GridCell <LifeCell>> gridCells = nghCreator.getNeighborhood (false);
		 
		
		 if(this.checkLiveness(gridCells)) {
			 
		//do nothing
			 
		 }else {
			 
			 Object obj = this;
			 cellsToRemove.add(obj);
			 
			
			/* Life zombie = new Zombie (space , grid );
			 context .add ( zombie );
			 space . moveTo (zombie , spacePt . getX (), spacePt . getY ());
			 grid . moveTo ( zombie , pt. getX (), pt. getY ());*/
			 
		 }
		 
		 for (GridCell<LifeCell> gridCell : gridCells) {
			
			 this.checkNeighbors(gridCell);
		}
		 
		 
		 
		 
		 if(agentsToCheck == 0 && cellsToRemove.size() != 0) {
			 this.removeCell();
			 }
		 
		 
	/*	 for (GridCell<LifeCell> gridCell : gridCells) {
			 
			List<LifeCell> cellsList = (List<LifeCell>) gridCell.items();
			
			
			 cellsList.get(0).checkNeighbors(gridCell);
		}*/
		 
		 /*Without the shuffle, the Cells will always move in the same direction when
		 all cells are equal.*/
	//	 SimUtilities.shuffle ( gridCells , RandomHelper.getUniform ());
		 
		}
	
	
	private void removeCell() {
		
		   for (Object obj : cellsToRemove) {
			   Context <Object> context = ContextUtils.getContext(obj);
				 context.remove (obj);
		}
		   
		   agentsCardinality = agentsCardinality - cellsToRemove.size();
		   
		   if(agentsCardinality == 0) {
			   agentsCardinality = getCardinality(); 
		   }
		   agentsToCheck = agentsCardinality;
		   cellsToRemove.clear();
		
	}

	public void checkNeighbors(GridCell<LifeCell> gridCell) {
		
		// get the grid location of this Cell
		   GridPoint pt = grid.getLocation(gridCell);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		   GridCellNgh <LifeCell> nghCreator = new GridCellNgh <LifeCell>( grid , pt , LifeCell.class , 1, 1);
				 
		// import repast . simphony . query . space . grid . GridCell
		   List< GridCell <LifeCell>> gridCells = nghCreator.getNeighborhood (true);
		   
		   if(this.checkLiveness(gridCells)) {
				 
			   Object obj = this;
		
				 cellsToRemove.add(obj);
				 LifeCell newCell = new LifeCell(grid, true);
				  Context <Object> context = ContextUtils.getContext(obj);
				 context .add (newCell);
		
				 grid.moveTo (newCell, pt. getX (), pt. getY ());
				 }
		   	   
		
	}
	
	
	public boolean checkLiveness(List<GridCell<LifeCell>> neighbCells){
		
		int countCells = 0;
		int num= 0;

		
		for (GridCell<LifeCell> gridCell : neighbCells) {
			
			 num = gridCell.size();
			 
			 if (num == 1) {
				countCells++;
			}
	
		}
		agentsToCheck--;
		// If the cell has less than 2 neighbors, it will die for starvation
		// If the cell has 2 neighbors stays alive
		// If the cell has 3 neighbors and is not alive, it will be born
		// If the cell as more than 3 neighbors, it will die for overpopulation
		
		if (countCells < 2 || countCells > 3) {
			
			return this.isAlive = false;
		}else {
			
			return this.isAlive = true;
		}
		
				
		
	}
		
	
	
}
