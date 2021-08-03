/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
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
	private String[] typeList = {"helper","suppressor","naive","activated"};
	protected String type2 = "";
	protected int antigenId = 0;
	protected static int bcells = getBcells();
	protected static int antibodiesToKill;
	
	private Grid<Object> grid;

	
	public Tcell(Grid<Object> grid, String type,int bcells,String type2) {
		super();
		
		this.grid = grid;
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
		
		
		  if (this.type2 == "naive" && this.lookForApc(gridCells)) {

				Antigen antigen = this.getAntigenFromApc(gridCells);

				if (antigen != null) {
					this.antigenId = antigen.id;
					this.type2 = typeList[3];
				}

	
		
		  }else {
			  
				
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

 			grid.moveTo(this, pt.getX(), pt.getY());
			//moved = true;
		}
		
	}


public void suppress() {
	
	
	
}

	
	
	
}
