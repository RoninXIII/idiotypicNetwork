/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.List;

import bsh.This;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author mario
 *
 */
public class Antigen {
	
	
	
	private Grid<Object> grid;
	protected int id;
	protected String color = "";
	
	
	public Antigen(Grid<Object> grid, int id) {
		super();
		
		this.grid = grid;
		this.id = id;
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
		
		if (!this.die(gridCells)) {
			
		
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
	
	public boolean die(List<GridCell<Object>> gridCells) {
		
		List<Antibody> antibodies = new ArrayList<>() ;
		int killers = 0;
		
		for (GridCell<Object> gridCell : gridCells) {
			
			if (gridCell.size() != 0 &&  gridCell.items().toString().contains("Antibody")) {
				antibodies.add(this.getAntibody((List<Object>)gridCell.items()));
			}
		}
		
		if (antibodies.size() > 1) {
			
			for (Antibody object : antibodies) {
				
				if (object.type.equalsIgnoreCase("killer") && object.id == this.id) {
					killers++;
				}
			}
			
			if(killers > 1) {
				
				Context<Object> context = ContextUtils.getContext(this);
				context.remove(this);
				return true;
			}else return false;
			
		
		}else {
	
			return false;
		} 
		
		
	}
	
	
	public Antibody getAntibody(List<Object> list) {
		Antibody b = (Antibody)list.get(0);
		
		if (b.type.equalsIgnoreCase("killer")) {
			return b;
		}else return null;
	
		
	}
	
	public void moveTowards(GridPoint pt) {
		
		if (!pt.equals(grid.getLocation(this))) {

 			grid.moveTo(this, pt.getX(), pt.getY());
			//moved = true;
		}
		
	}
	
	
}
