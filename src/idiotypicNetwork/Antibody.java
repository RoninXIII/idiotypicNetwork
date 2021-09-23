/**
 * 
 */
package idiotypicNetwork;

import java.sql.Time;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
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
public class Antibody {

	private Grid<Object> grid;
	protected int id;
	protected int antigenId;
	protected int lifeSpan = 0;
	//protected int concentration;
	//protected String type = "";
	//private String[] typeList = { "killer", "memoryKeeper" };
	private ContinuousSpace<Object> space;
	public static int time;

	public Antibody(ContinuousSpace<Object> space, Grid<Object> grid, int id,int antigenId) {
		super();
		this.grid = grid;
		this.id = id;
		this.antigenId = antigenId;	
		this.space = space;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Antigen> nghCreator = new GridCellNgh<Antigen>(grid, pt, Antigen.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Antigen>> gridCells = nghCreator.getNeighborhood(false);

		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		GridPoint freeCell = null;

		for (GridCell<Antigen> gridCell : gridCells) {

			if (gridCell.size() == 0) {
				freeCell = gridCell.getPoint();

			}

		}

		if (freeCell != null) {

			this.moveTowards(freeCell);

		}
		
		lifeSpan++;
		
		if (lifeSpan == 400) {
			die();
		}

	}
	
	
	
	// After a certain time the antibodies will decay
	public void die() {
		
		
		Context<Object> context = ContextUtils.getContext(this);
		context.remove(this);
		
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
