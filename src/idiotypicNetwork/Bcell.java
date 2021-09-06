/**
 * 
 */
package idiotypicNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
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
	private String[] typeList = { "naive", "activated" };
	private ContinuousSpace<Object> space;
	protected int id;
	protected int antigenId;
	public static int countAntibodies;

	/*
	 * The Cell will move about the ContinuousSpace and we will simply round the
	 * ContinuousSpace location to determine the corresponding Grid location
	 */
	private Grid<Object> grid;

	public Bcell(ContinuousSpace<Object> space, Grid<Object> grid, String type, int id) {
		super();

		this.grid = grid;
		this.space = space;
		this.type = type;
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
		// GridPoint freeCellForClone = null;

		if (this.lookForAntigen(gridCells) && this.type == "activated") {

			Antigen antigen = (Antigen) this.getAntigen(gridCells);

			if (this.antigenId == antigen.id) {
				// this.cloneCell(gridCells);
				nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1);

				// import repast . simphony . query . space . grid . GridCell
				gridCells = nghCreator.getNeighborhood(false);

				SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

				this.releaseAntiBodies(gridCells);
			}

		} else if (this.lookForAntigen(gridCells) && this.type == "naive") {
			Antigen antigen = this.getAntigen(gridCells);
			this.type = typeList[1];
			this.antigenId = antigen.id;

		} else if (this.lookForTcell(gridCells)) {
			List<Tcell> tcells = this.getTcells(gridCells);

			for (Tcell tcell : tcells) {
				if (tcell.type == "helper" && tcell.type2 == "activated" && tcell.antigenId == this.antigenId) {
					this.type = typeList[1];
					this.releaseMoreAntibodies(gridCells);
					return;
				} else {

				}
			}

		} else {

			for (GridCell<Object> gridCell : gridCells) {

				if (gridCell.size() == 0) {
					freeCell = gridCell.getPoint();

				}

			}
		}

		if (freeCell != null) {
			// this.type = typeList[1];
			this.moveTowards(freeCell);

		}
	}

	private void releaseMoreAntibodies(List<GridCell<Object>> gridCells) {
		Context<Object> context = ContextUtils.getContext(this);
		Network < Object > net = (Network<Object>) context.getProjection("antibodies network");
		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() == 0) {
				GridPoint pt = gridCell.getPoint();
				Antibody ab = new Antibody(space, grid, id, this.antigenId);
				context.add(ab);
				net.addEdge ( this , ab );
				grid.moveTo(ab, pt.getX(), pt.getY());

			}
		}
		// countAntibodies++;
		
		

	}

	private List<Tcell> getTcells(List<GridCell<Object>> gridCells) {

		List<Tcell> tcells = new ArrayList<>();
		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Tcell")) {

				for (Object obj : gridCell.items()) {
					if (obj instanceof Tcell) {

						tcells.add((Tcell) obj);
					}
				}

			}
		}

		return tcells;
	}

	private Antigen getAntigen(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Antigen")
					&& !gridCell.items().toString().contains("AntigenPresentingCell")) {

				for (Object obj : gridCell.items()) {
					if (obj instanceof Antigen) {
						Antigen antigen = (Antigen) obj;
						return antigen;
					}

				}

			}
		}

		return null;
	}

	public boolean lookForAntigen(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Antigen")
					&& !gridCell.items().toString().contains("PresentingCell")) {
				return true;
			}
		}

		return false;
	}

	public boolean lookForTcell(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() != 0 && gridCell.items().toString().contains("Tcell")) {

				return true;
			}
		}

		return false;

	}

	public boolean cloneCell(List<GridCell<Object>> gridCells) {

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() == 0) {
				Context<Object> context = ContextUtils.getContext(this);
				GridPoint pt = gridCell.getPoint();
				Bcell newCell = new Bcell(space, grid, "naive", this.id);
				context.add(newCell);
				grid.moveTo(newCell, pt.getX(), pt.getY());
				return true;
			}
		}

		return false;

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

	public void releaseAntiBodies(List<GridCell<Object>> gridCells) {

		int releasedAntibodies = 0;
		Context<Object> context = ContextUtils.getContext(this);

		for (GridCell<Object> gridCell : gridCells) {

			if (gridCell.size() == 0 && releasedAntibodies < 2) {
				GridPoint pt = gridCell.getPoint();
				Antibody ab = new Antibody(space, grid, id, this.antigenId);
				context.add(ab);
				grid.moveTo(ab, pt.getX(), pt.getY());
				releasedAntibodies++;
			}
		}
		// countAntibodies++;

	}

	public List<GridCell<Bcell>> getNeighbors(GridCell<Bcell> cell) {

		// get the grid location of this Cell
		GridPoint pt = cell.getPoint();

		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood.
		GridCellNgh<Bcell> nghCreator = new GridCellNgh<Bcell>(grid, pt, Bcell.class, 1, 1);

		// import repast . simphony . query . space . grid . GridCell
		List<GridCell<Bcell>> gridCells = nghCreator.getNeighborhood(true);

		return gridCells;

	}

}
