/**
 * 
 */
package idiotypicNetwork;

import java.awt.Color;
import java.awt.Font;

import saf.v3d.scene.Position;

import repast.simphony.gis.styleEditor.SimpleMarkFactory;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author mario
 *
 */
public class idiotypicNetworkStyleTcell extends DefaultStyleOGL2D {
	
	

	
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
	    public int getBorderSize(Object agent) {
	        return 4;
	    }
	   
	    
	    @Override
		public String getLabel(Object object) {
	   	   Tcell t;
    	   
	 	    	if(object instanceof Tcell) {
	 	        	
	 	        	t = (Tcell) object;
	 	        	
	 	        	if (t.type2 == "activated") {
						
	 	        		return "Th-An"+t.antigenId;
					}else return "T";
	 	        } else {
	 	            return "";
	 	        }
	
		}

	 
	    
	    @Override
		public Color getBorderColor(Object object) {
		
     Tcell t;
	        
	        if(object instanceof Tcell) {
	        	
	        	t = (Tcell) object;
	        	
	        	 
	        	if (t.type2 == "naive") {
		            return Color.RED;
		        } else {
		            return Color.GREEN;
		        }
		        
		        
	        } else {
	            return Color.BLACK;
	        }
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
	    	
	    	if (object instanceof Tcell) {
				return Position.SOUTH; 
			}else return null;
	    }

		@Override
	    public Color getColor(Object object) {
	        Tcell t;
	        
	        if(object instanceof Tcell) {
	        	
	        	t = (Tcell) object;
	        	
	        	 
	        	if (t.type == "helper") {
		            return Color.GREEN;
		        } else {
		            return Color.RED;
		        }
		        
		        
	        } else {
	            return Color.BLACK;
	        }
	       
	       
    
	        
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
