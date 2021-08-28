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
public class Antigen {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	protected int id;
	protected String color = "";

	public Antigen(ContinuousSpace<Object> space, Grid<Object> grid, int id) {
		super();

		this.grid = grid;
		this.id = id;
		this.space = space;
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

		List<Antibody> antibodies = new ArrayList<>();
		int killers = 0;

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Antibody")) {

				antibodies.addAll(this.getAntibody((List<Object>) gridCell.items()));
			}
		}

		if (antibodies.size() > 1) {

			for (Antibody object : antibodies) {

				if (object.id == this.id) {
					killers++;
				}
			}

			if (killers > 1) {

				Tcell tcell = new Tcell(space, grid, "", 0, "");
				tcell.antibodiesToKill.add(this.id);
				Context<Object> context = ContextUtils.getContext(this);
				context.remove(this);
				return true;
			} else
				return false;

		} else {

			return false;
		}

	}

	// Retrieve the list of all Antibodies in a cell
	public List<Antibody> getAntibody(List<Object> list) {
		List<Antibody> list2 = new ArrayList<>();

		for (Object obj : list) {
			if (obj instanceof Antibody) {			
				list2.add((Antibody) obj);
			}

		}
		
		return list2;



	}

	public void moveTowards(GridPoint pt) {

		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
			// moved = true;
		}

	}

}
