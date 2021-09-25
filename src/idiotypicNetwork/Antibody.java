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
		GridCellNgh<Antigen> nghCreator = new GridCellNgh<Antigen>(grid, pt, Antigen.class, 1, 1, 1);

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
		double heading = 0;
		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY(),pt.getZ());
			// Randomly change the current heading plus or minus 50 degrees
			double sgn = RandomHelper.nextDoubleFromTo(-10, 10);       // a value between -0.5 and 0.5
			if (sgn > 0)
				heading = heading + RandomHelper.nextDoubleFromTo(0, 50);
			else
				heading = heading - RandomHelper.nextDoubleFromTo(0, 50);

			// Move the agent on the space by one unit according to its new heading
			space.moveByVector(this, 1, Math.toRadians(heading),0,0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY(),(int)myPoint.getZ());
			// moved = true;
		}

	}



}
