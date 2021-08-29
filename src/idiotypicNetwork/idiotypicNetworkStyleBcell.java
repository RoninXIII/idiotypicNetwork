package idiotypicNetwork;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.gis.styleEditor.SimpleMarkFactory;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

public class idiotypicNetworkStyleBcell extends DefaultStyleOGL2D {

	
	 private static SimpleMarkFactory markFac = new SimpleMarkFactory();

	    @Override
	    public void init(ShapeFactory2D factory) {
	        super.init(factory);
	    }

	    @Override
	    public VSpatial getVSpatial(Object agent, VSpatial spatial) {
	        if (spatial == null) {
	            spatial = shapeFactory.createShape(markFac.getMark("triangle"), true);
	           
	        }
	        return spatial;
	    }
	    
	    
	    @Override
	    public int getBorderSize(Object agent) {
	        return 4;
	    }
	    
	    

	    @Override
	    public Color getColor(Object object) {
	        Bcell a;
	      
	        if (object instanceof Bcell) {
	            a = (Bcell) object;
	            
	            if (a.type == "naive") {
		            return Color.RED;
		        } else {
		            return Color.GREEN;
		        }
	            
	        } else {
	            return Color.BLACK;
	        }
	       
	       
	       
	        
	        
	    }
	    
	    
	    @Override
	  		public String getLabel(Object object) {
	  	   
	      	   Bcell b ;
	  	 	    	if(object instanceof Bcell) {
	  	 	    		
	       		        b = (Bcell) object;
	       		     if (b.type == "naive") {
	       		    	 return "B"+b.id;
						}else return "B"+b.id+"-An"+b.antigenId;
	  	 		       
	  	 	        } else {
	  	 	            return "";
	  	 	        }
	  	
	  		}
	    
	    @Override
		public Color getBorderColor(Object object) {
		
			return Color.BLACK;
		}
	    
	    
	    @Override
		public Font getLabelFont(Object object) {
		
	
			return Font.decode(Font.SANS_SERIF);
		}
	    
	    
	    @Override
		public Color getLabelColor(Object object) {
			
			return Color.BLACK;
		}

		@Override
	    public Position getLabelPosition(Object object) {
	    	
	    	if (object instanceof Bcell) {
				return Position.SOUTH; 
			}else return null;
	    }

	    @Override
	    public float getRotation(Object object) {
	        return 0;
	    }

	    @Override
	    public float getScale(Object object) {
	        return 20;
	    }
	    
}
