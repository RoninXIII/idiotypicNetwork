/**
 * 
 */
package idiotypicNetwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	
	
	private BufferedImage img = null; 
	private BufferedImage img2 = null;


    @Override
    public void init(ShapeFactory2D factory) {
        super.init(factory);
    }

    @Override
    public VSpatial getVSpatial(Object agent, VSpatial spatial) {
    	
    	if (agent instanceof Tcell) {
			Tcell tcell = (Tcell) agent;
			
			try {
	    	    img = ImageIO.read(new File("C:\\Users\\Mario\\eclipse-workspace-repast\\idiotypicNetwork\\idiotypicNetwork\\icons\\tcell.png"));
	    	    img2 = ImageIO.read(new File("C:\\Users\\Mario\\eclipse-workspace-repast\\idiotypicNetwork\\idiotypicNetwork\\icons\\tcell2.png"));
	    	} catch (IOException e) {
	    		
	    	}
	        if (spatial == null && tcell.type2 == "naive") {
	            spatial = shapeFactory.createImage("t", img2);
	           
	        }else if (spatial != null && tcell.type2 == "activated") {
	        	spatial = shapeFactory.createImage("t2", img);
			}
		} else {
			
		
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
					}else return "T "+t.type;
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
	        return 0.35f;
	    }
}
