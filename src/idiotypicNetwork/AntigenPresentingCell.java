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


// Once recognized "swallows" the antigen and presents it through Major histocompatibility complex (MHC). 
public class AntigenPresentingCell {

	 
	private Grid<Object> grid;
	protected List<Antigen> antigensToPresent = new ArrayList<>();
	
	
	public AntigenPresentingCell(Grid<Object> grid, List<Antigen> antigensToPresent) {
		super();
		this.grid = grid;
		this.antigensToPresent = antigensToPresent;
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
			
			
			if (gridCell.items().toString().contains("Antigen") && !gridCell.items().toString().contains("PresentingCell")) {
				Antigen antigen = this.getAntigen((List<Object>) gridCell.items());
				antigensToPresent.add(antigen);
			}
			
		}
		
	for (GridCell<Object> gridCell : gridCells) {
			
			if(gridCell.size() == 0){
				
				freeCell = gridCell.getPoint();
			}
			
		}
		
		if (freeCell != null) {
			
			this.moveTowards(freeCell);
			
		}
		



	}
	
	public Antigen getAntigen(List<Object> item) {
		
		Antigen antigen = (Antigen) item.get(0);
		
		return antigen;
	}
	
	
public void moveTowards(GridPoint pt) {
		
		if (!pt.equals(grid.getLocation(this))) {

 			grid.moveTo(this, pt.getX(), pt.getY());
			//moved = true;
		}
		
	}
	
	
}