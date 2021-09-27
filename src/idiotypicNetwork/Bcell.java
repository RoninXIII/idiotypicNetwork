/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.util.DirectionalGradientPaintTransformer;

import com.sun.source.util.TreePathScanner;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.relogo.ide.dynamics.NetLogoSystemDynamicsParser.intg_return;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author mario
 *
 */
public class Bcell {

	protected String type = ""; // check whether the cell is alive or not
	protected String[] typeList = { "naive", "activated" };
	protected ContinuousSpace<Object> space;
	protected int id;
	protected int antigenId;
	// Keep track of the number of the antibodies with a certain id
	public static HashMap<Integer, Integer> antibodiesHashMap = new HashMap<Integer, Integer>();
	protected static int countAntibodies;
	private boolean hasClone = false;
	// Will be set to the tick on which the bcell is activated
	protected double timeToReleaseAntibodies = 0;
	/*
	 * The Cell will move about the ContinuousSpace and we will simply round the
	 * ContinuousSpace location to determine the corresponding Grid location
	 */
	protected Grid<Object> grid;

	public Bcell(ContinuousSpace<Object> space, Grid<Object> grid, String type, int id) {
		super();

		this.grid = grid;
		this.space = space;
		this.type = type;
		this.id = id;
	}

	public Bcell(ContinuousSpace<Object> space, Grid<Object> grid, String type, int id, int antigenId) {
		super();

		this.grid = grid;
		this.space = space;
		this.type = type;
		this.id = id;
		this.antigenId = antigenId;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		// get the grid location of this Cell
		GridPoint pt = grid.getLocation(this);

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Antigen> antigenNghCreator = new GridCellNgh<Antigen>(grid, pt, Antigen.class, 1, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Antigen>> antigensGridCells = antigenNghCreator.getNeighborhood(false);

		SimUtilities.shuffle(antigensGridCells, RandomHelper.getUniform());

		Context<Object> context = ContextUtils.getContext(this);
		// Aggiungere caso di generazione di anticorpi dopo un certo periodo

		if (this.type == "naive" && this.lookForAntigen()) {
			Antigen antigen = this.getAntigen();
			this.type = typeList[1];
			this.antigenId = antigen.id;
			this.timeToReleaseAntibodies = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			return;
		} else if (this.type == "activated" && this.lookForAntigen()
				&& RunEnvironment.getInstance().getCurrentSchedule().getTickCount() >= timeToReleaseAntibodies + 200) {

			Antigen antigen = (Antigen) this.getAntigen();

			if (hasClone == false & this.antigenId == antigen.id) {
				this.cloneCell();
				this.hasClone = true;
				this.releaseAntiBodies();
			} else if (hasClone == true & this.antigenId == antigen.id) {

				this.releaseAntiBodies();
			}
			return;

		} else if (this.type == "naive" && this.lookForTcell()) {
			Tcell tcell = this.getTcell();
		
			if (tcell.type == "helper" && tcell.type2 == "activated" && tcell.antigenId == this.antigenId) {
				this.type = typeList[1];
				this.antigenId = tcell.antigenId;
				this.timeToReleaseAntibodies = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				this.releaseMoreAntibodies();
				return;
			}

		} else if (this.type == "activated" && this.lookForTcell()) {

			Tcell tcell = this.getTcell();
			
			if (tcell.type == "helper" && tcell.type2 == "activated" && tcell.antigenId == this.antigenId) {
				this.releaseMoreAntibodies();
				return;
			}

		}


		GridPoint pointWithMostAntigens = null;
		int maxCount = -1;
		for (GridCell<Antigen> gridCell : antigensGridCells) {

			if (gridCell.size() > maxCount) {
				pointWithMostAntigens = gridCell.getPoint();
				maxCount = gridCell.size();
			}
		}

		this.moveTowards(pointWithMostAntigens);

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

	public void releaseAntiBodies() {

		int releasedAntibodies = 0;
		Context<Object> context = ContextUtils.getContext(this);
		Network<Object> net = (Network<Object>) context.getProjection("antibodies network");
		GridPoint pt = grid.getLocation(this);

		for (int i = 0; i < 3; i++) {

			Antibody ab = new Antibody(space, grid, id, this.antigenId);
			context.add(ab);
			net.addEdge(this, ab);
			grid.moveTo(ab, pt.getX(), pt.getY(), pt.getZ());

			releasedAntibodies++;

			if (antibodiesHashMap.containsKey(antigenId)) {
				antibodiesHashMap.put(this.antigenId, antibodiesHashMap.get(this.antigenId) + 1);
			} else {
				antibodiesHashMap.put(this.antigenId, releasedAntibodies);
			}

		}

	}

	public void releaseMoreAntibodies() {
		Context<Object> context = ContextUtils.getContext(this);
		Network<Object> net = (Network<Object>) context.getProjection("antibodies network");
		GridPoint pt = grid.getLocation(this);

		for (int i = 0; i < 6; i++) {
			Antibody ab = new Antibody(space, grid, id, this.antigenId);
			context.add(ab);
			net.addEdge(this, ab);
			grid.moveTo(ab, pt.getX(), pt.getY(), pt.getZ());
			space.moveTo(ab, pt.getX(), pt.getY(), pt.getZ());
			if (antibodiesHashMap.containsKey(antigenId)) {
				antibodiesHashMap.put(this.antigenId, antibodiesHashMap.get(this.antigenId) + 1);
			} else {
				antibodiesHashMap.put(this.antigenId, 1);
			}
		}

	}

	public Tcell getTcell() {

		GridPoint pt = grid.getLocation(this);

		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY(), pt.getZ())) {
			if (obj instanceof Tcell) {
				return (Tcell) obj;
			}
		}

		return null;
	}

	public Antigen getAntigen() {

		GridPoint pt = grid.getLocation(this);

		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY(), pt.getZ())) {
			if (obj instanceof Antigen) {
				return (Antigen) obj;
			}
		}

		return null;
	}

	public boolean lookForAntigen() {

		GridPoint pt = grid.getLocation(this);

		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY(), pt.getZ())) {
			if (obj instanceof Antigen) {
				return true;
			}
		}

		return false;
	}

	public boolean lookForTcell() {

		GridPoint pt = grid.getLocation(this);

		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY(), pt.getZ())) {
			if (obj instanceof Tcell) {
				return true;
			}
		}

		return false;

	}

	public void cloneCell() {

		List<GridCell<Object>> gridCells = this.getNeighborhood();
		int index = RandomHelper.nextIntFromTo(0, gridCells.size() - 1);
		GridCell<Object> gridCell = gridCells.get(index);
		Context<Object> context = ContextUtils.getContext(this);
		GridPoint pt = grid.getLocation(this);
		NdPoint spacePt = space.getLocation(this);
		MemoryKeeperCell cell = new MemoryKeeperCell(space, grid, "activated", this.id, this.antigenId);
		context.add(cell);
		grid.moveTo(cell, pt.getX(), pt.getY(), pt.getZ());
		space.moveTo(cell, spacePt.getX(), spacePt.getY(), spacePt.getZ());

	}

	public void moveTowards(GridPoint pt) {

		if (!pt.equals(grid.getLocation(this))) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY(), pt.getZ());

			// Move the agent on the space by one unit according to its new heading
			// space.moveByVector(this, 1, Math.toRadians(heading),0,0);
			space.moveTo(this, otherPoint.getX(), otherPoint.getY(), otherPoint.getZ());
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY(), (int) myPoint.getZ());
			// moved = true;
		}

	}

}
