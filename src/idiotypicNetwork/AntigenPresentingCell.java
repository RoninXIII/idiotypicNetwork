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
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

/**
 * @author mario
 *
 */

// Once recognized "swallows" the antigen and presents it through Major histocompatibility complex (MHC). 
public class AntigenPresentingCell {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	protected List<Antigen> antigensToPresent = new ArrayList<>();

	public AntigenPresentingCell(ContinuousSpace<Object> space, Grid<Object> grid, List<Antigen> antigensToPresent) {
		super();
		this.grid = grid;
		this.antigensToPresent = antigensToPresent;
		this.space = space;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() { 

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Antigen> nghCreator = new GridCellNgh<Antigen>(grid, pt, Antigen.class, 1, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Antigen>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		GridPoint freeCell = null;
		for (GridCell<Antigen> gridCell : gridCells) {

			if (gridCell.size() != 0) {
				List<Antigen> antigens = (List<Antigen>) gridCell.items();
				antigensToPresent.addAll(antigens);
			}
			
			if (gridCell.size() == 0) {

				freeCell = gridCell.getPoint();
			}

		}


		if (freeCell != null) {

			this.moveTowards(freeCell);

		}

	}

	public List<Antigen> getAntigens(List<Object> list) {

		List<Antigen> list2 = new ArrayList<>();

		for (Object obj : list) {
			if (obj instanceof Antigen) {			
				list2.add((Antigen) obj);
			}

		}
		
		return list2;
	}

	public void moveTowards(GridPoint pt) {
		double heading = 0;
				if (!pt.equals(grid.getLocation(this))) {
					NdPoint myPoint = space.getLocation(this);
					NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY(),pt.getZ());
					// Randomly change the current heading plus or minus 50 degrees
					double sgn = RandomHelper.nextDoubleFromTo(-0.5, 0.5);       // a value between -0.5 and 0.5
					if (sgn > 0)
						heading = heading + RandomHelper.nextDoubleFromTo(0, 50);
					else
						heading = heading - RandomHelper.nextDoubleFromTo(0, 50);

					// Move the agent on the space by one unit according to its new heading
					//space.moveByVector(this, 1, Math.toRadians(heading),0,0);
					space.moveTo(this, otherPoint.getX(),otherPoint.getY(),otherPoint.getZ());
					myPoint = space.getLocation(this);
					grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY(),(int)myPoint.getZ());
					// moved = true;
				}

			}

}
