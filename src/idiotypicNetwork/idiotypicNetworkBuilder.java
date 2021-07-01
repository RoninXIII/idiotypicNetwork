package idiotypicNetwork;

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
		context.setId ("idiotypicNetwork");
		
		int x = 10, y=10;
	//	ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory ( null );
		//ContinuousSpace <Object > space = spaceFactory.createContinuousSpace("space", context , new RandomCartesianAdder <Object >(), new repast.simphony.space.continuous.WrapAroundBorders (), x, y);
				
				 GridFactory gridFactory = GridFactoryFinder.createGridFactory ( null );
				 // Correct import : import repast . simphony . space . grid . WrapAroundBorders ;
				 Grid < Object > grid = gridFactory.createGrid ("grid", context , new GridBuilderParameters <Object >( new WrapAroundBorders (), new SimpleGridAdder <Object >() ,
				 false , x, y));
				 
				 
				 Parameters params = RunEnvironment.getInstance ().getParameters();
				 int cellCount = params.getInteger("cell_count");
				  
				 // add alive cells
				  for (int i = 0; i < cellCount ; i++) {
				  context .add (new LifeCell(grid, true));
				  }
				  // add dead cells
				  for (int i = 0; i < (x*y)-cellCount ; i++) {
					  context .add (new LifeCell(grid, false));
					  }
		
				  for ( Object obj : context ) {
					  // NdPoint pt = space . getLocation (obj );
					  int randomNum = ThreadLocalRandom.current().nextInt(0, x + 1);
					  int randomNum2 = ThreadLocalRandom.current().nextInt(0, x + 1);
					  
					  while(!grid.moveTo (obj , randomNum, randomNum2)) {
						  
						  randomNum = ThreadLocalRandom.current().nextInt(0, x + 1);
						  randomNum2 = ThreadLocalRandom.current().nextInt(0, x + 1);
					  }
				
				
					   }
		
		return context;
	}

}
