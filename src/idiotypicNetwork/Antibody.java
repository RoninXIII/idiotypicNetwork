/**
 * 
 */
package idiotypicNetwork;

import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

/**
 * @author mario
 *
 */
public class Antibody {

	private Grid<Object> grid;
	protected int id;
	protected String type = "";
	private String[] typeList = {"killer","memoryKeeper"};
	
	
	public Antibody(Grid<Object> grid, int id, String type) {
		super();
		this.grid = grid;
		this.id = id;
		this.type = type;
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
		
	
			
		
		for (GridCell<Object> gridCell : gridCells) {
			
			
			if (gridCell.size() == 0) {
				freeCell = gridCell.getPoint();
				
			}
			
		}
		
		if (freeCell != null) {
			
			this.moveTowards(freeCell);
			
		}
	

	}
	
	
public void moveTowards(GridPoint pt) {
		
		if (!pt.equals(grid.getLocation(this))) {

 			grid.moveTo(this, pt.getX(), pt.getY());
			//moved = true;
		}
		
	}
	
	
	//This Watch will watch for any changes to a "moved" variable in the Zombies class.
			//Our query will return true when the Zombie that moved is within the Moore neighborhood (8 surrounding grid cells) of the
			//Human whose Watch is currently being evaluated.
	/*		@Watch(watcheeClassName = "idiotypicNetwork.Bcell",watcheeFieldNames = "moved",query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
			public void run() {
				
				
	
			}*/
	
	
	
	
}
