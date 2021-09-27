/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.relogo.ide.dynamics.NetLogoSystemDynamicsParser.intg_return;
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
	protected int lifeSpan = 0;

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
		GridCellNgh<Antibody> nghCreator = new GridCellNgh<Antibody>(grid, pt, Antibody.class, 1, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Antibody>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		if (!this.die(gridCells)) {

			int pos = RandomHelper.nextIntFromTo(0, gridCells.size() - 1);
			GridCell gridCell = gridCells.get(pos);

			this.moveTowards(gridCell.getPoint());

		}

		lifeSpan++;

		if (lifeSpan == 150) {
			cloneAntigen();
		}

	}

	public List<GridCell<Object>> getNeighborhood() {

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		return gridCells;

	}

	public void cloneAntigen() {

		List<GridCell<Object>> gridCells = this.getNeighborhood();
		int index = RandomHelper.nextIntFromTo(0, gridCells.size() - 1);
		GridCell<Object> gridCell = gridCells.get(index);
		Context<Object> context = ContextUtils.getContext(this);
		GridPoint pt = grid.getLocation(this);
		NdPoint spacePt = space.getLocation(this);
		Antigen newAntigen = new Antigen(space, grid, this.id);
		context.add(newAntigen);
		grid.moveTo(newAntigen, pt.getX(), pt.getY(), pt.getZ());
		space.moveTo(newAntigen, spacePt.getX(), spacePt.getY(), spacePt.getZ());

	}

	public boolean die(List<GridCell<Antibody>> gridCells) {

		List<Antibody> antibodies = new ArrayList<>();
		int killers = 0;

		for (GridCell<Antibody> gridCell : gridCells) {

			if (gridCell.size() != 0) {

				antibodies.addAll((List<Antibody>) gridCell.items());
			}
		}

		if (antibodies.size() > 1) {

			for (Antibody object : antibodies) {

				if (object.antigenId == this.id) {
					killers++;
				}
			}

			if (killers > 1) {

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
	/*
	 * public List<Antibody> getAntibody(List<Antibody> list) { List<Antibody> list2
	 * = new ArrayList<>();
	 * 
	 * for (Object obj : list) { if (obj instanceof Antibody) { list2.add((Antibody)
	 * obj); }
	 * 
	 * }
	 * 
	 * return list2;
	 * 
	 * }
	 */

	public int getAntigensNumber(int id) {

		Context<Object> context = ContextUtils.getContext(this);
		int count = 0;
		for (int i = 0; i < context.getObjects(Antigen.class).size(); i++) {
			Antigen antigen = (Antigen) context.getObjects(Antigen.class).get(i);
			if (antigen.id == id) {
				count++;
			}
		}

		return count;
	}

	public void moveTowards(GridPoint pt) {
		double heading = 0;
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY(), pt.getZ());
			// Randomly change the current heading plus or minus 50 degrees
			double sgn = RandomHelper.nextDoubleFromTo(-0.5, 0.5); // a value between -0.5 and 0.5
			if (sgn > 0)
				heading = heading + RandomHelper.nextDoubleFromTo(0, 50);
			else
				heading = heading - RandomHelper.nextDoubleFromTo(0, 50);

			// Move the agent on the space by one unit according to its new heading
			// space.moveByVector(this, 1, Math.toRadians(heading),0,0);
			space.moveTo(this, otherPoint.getX(), otherPoint.getY(), otherPoint.getZ());
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY(), (int) myPoint.getZ());
			// moved = true;
		}

	}

}
