package idiotypicNetwork;

import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class MemoryKeeperCell extends Bcell {

	public MemoryKeeperCell(ContinuousSpace<Object> space, Grid<Object> grid, String type, int id, int antigenId) {
		super(space, grid, type, id, antigenId);
		// TODO Auto-generated constructor stub

	}

	@Override
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

		if (this.lookForAntigen()) {

			Antigen antigen = (Antigen) this.getAntigen();

			this.releaseAntiBodies();

			return;

		} else if (this.lookForTcell()) {
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

}
