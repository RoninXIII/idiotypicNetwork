package idiotypicNetwork;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.watcher.query.NamedWithinBooleanExpression;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class idiotypicNetworkBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("antibodies network", context, true);
		netBuilder.buildNetwork();

		context.setId("idiotypicNetwork");

		int x = 20, y = 20, z = 20;
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context,
				new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), x, y, z);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		// Correct import : import repast . simphony . space . grid . WrapAroundBorders;
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, x, y, z));

		Parameters params = RunEnvironment.getInstance().getParameters();
		int bCellCount = params.getInteger("cell_count");
		//int tCellCount = params.getInteger("Tcell_count");
		int antigenCount = params.getInteger("Antigen_count");
		int apcCount = params.getInteger("APC_count");

		String[] typeList = { "helper", "suppressor" };

		
		// add B cells
		for (int i = 0; i < bCellCount; i++) {
			// Bcell(grid,"naive or activated", id)
			context.add(new Bcell(space, grid, "naive", i));
		}

		// add blood cells
	/*	for (int i = 0; i < (x * y * 3); i++) {
			// Bcell(grid,"naive or activated", id)
			context.add(new Blood(grid, space));
		}*/

		// add T cells

		if (antigenCount <= 3) {

		
		context.add(new Tcell(space, grid, typeList[1], "naive"));
			
		
		} else if (antigenCount > 3 && antigenCount <= 10) {
			
			for (int j = 0; j < 3; j++) {
				if (j % 2 == 0) {
					context.add(new Tcell(space, grid, typeList[0], "naive"));
				} else {
					context.add(new Tcell(space, grid, typeList[1], "naive"));
				}

			}
			
		} 

		// add Antigens
		for (int j = 0; j < antigenCount; j++) {

			// int id = RandomHelper.nextIntFromTo(0, 3);
			context.add(new Antigen(space, grid, 0));
		}

		// add Antigens Presenting Cell
		for (int j = 0; j < apcCount; j++) {
			context.add(new AntigenPresentingCell(space, grid, new ArrayList<>()));
		}

		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY(),(int) pt.getZ());
		}

		return context;
	}

}
