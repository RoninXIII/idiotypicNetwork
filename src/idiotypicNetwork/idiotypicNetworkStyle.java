package idiotypicNetwork;

import java.awt.Color;

import repast.simphony.gis.styleEditor.SimpleMarkFactory;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class idiotypicNetworkStyle extends DefaultStyleOGL2D {

	
	 private static SimpleMarkFactory markFac = new SimpleMarkFactory();

	    @Override
	    public void init(ShapeFactory2D factory) {
	        super.init(factory);
	    }

	    @Override
	    public VSpatial getVSpatial(Object agent, VSpatial spatial) {
	        if (spatial == null) {
	            spatial = shapeFactory.createShape(markFac.getMark("circle"), true);
	        }
	        return spatial;
	    }

	    @Override
	    public Color getColor(Object object) {
	        LifeCell a;
	        if (object instanceof LifeCell) {
	            a = (LifeCell) object;
	        } else {
	            return Color.BLACK;
	        }
	        if (a.isAlive) {
	            return Color.BLACK;
	        } else {
	            return Color.WHITE;
	        }
	    }

	    @Override
	    public float getRotation(Object object) {
	        return 0;
	    }

	    @Override
	    public float getScale(Object object) {
	        return 10;
	    }
	    
}
