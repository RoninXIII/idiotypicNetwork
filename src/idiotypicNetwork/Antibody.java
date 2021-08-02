/**
 * 
 */
package idiotypicNetwork;

import java.util.List;


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
	
	
	//This Watch will watch for any changes to a "moved" variable in the Zombies class.
			//Our query will return true when the Zombie that moved is within the Moore neighborhood (8 surrounding grid cells) of the
			//Human whose Watch is currently being evaluated.
	/*		@Watch(watcheeClassName = "idiotypicNetwork.Bcell",watcheeFieldNames = "moved",query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
			public void run() {
				
				
	
			}*/
	
	
	
	
}
