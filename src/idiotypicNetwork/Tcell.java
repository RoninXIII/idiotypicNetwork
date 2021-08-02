/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
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
public class Tcell {

	protected String type = "";
	private String[] typeList = {"helper","suppressor"};
	
	private Grid<Object> grid;
	public static ArrayList<Object> cellsToRemove = new ArrayList<Object>();
	public static ArrayList<GridCell<Tcell>> cellsToAdd = new ArrayList<GridCell<Tcell>>();
	
	public Tcell(Grid<Object> grid, String type) {
		super();
		
		this.grid = grid;
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

	
	
	
}
