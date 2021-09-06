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

import repast.simphony.gis.styleEditor.SimpleMarkFactory;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

/**
 * @author mario
 *
 */
public class idiotypicNetworkStyleAntigen  extends DefaultStyleOGL2D {
	
	private BufferedImage img = null; 
	


    @Override
    public void init(ShapeFactory2D factory) {
        super.init(factory);
    }

    @Override
    public VSpatial getVSpatial(Object agent, VSpatial spatial) {
    	try {
    	    img = ImageIO.read(new File("C:\\Users\\Mario\\eclipse-workspace-repast\\idiotypicNetwork\\idiotypicNetwork\\icons\\virus.png"));
    	   
    	} catch (IOException e) {
    		
    	}
        if (spatial == null) {
            spatial = shapeFactory.createImage("c", img);
           
        }
        return spatial;
    }
	    
	    
	    @Override
	    public int getBorderSize(Object agent) {
	        return 5;
	    }
	    
	    

	    @Override
	    public Color getColor(Object object) {
	    	
	      Antigen a;
	        if (object instanceof Antigen) {
	        	
	        	a = (Antigen) object;
	        	a.color = "BLACK";
	           return Color.BLACK;
	            
	        } else {
	            return Color.WHITE;
	        }
	       
	    }
	    
	    
	    @Override
	  		public String getLabel(Object object) {
	    	Antigen a;
	      	   
	  	 	    	if(object instanceof Antigen) {
	  	 	    		a = (Antigen) object;
	  	 		        return "An"+a.id;
	  	 	        } else {
	  	 	            return "";
	  	 	        }
	  	
	  		}
	    
	    @Override
		public Color getBorderColor(Object object) {
		
			return Color.RED;
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
	    	
	    	if (object instanceof Antigen) {
				return Position.SOUTH; 
			}else return null;
	    }

	    @Override
	    public float getRotation(Object object) {
	        return 0;
	    }

	    @Override
	    public float getScale(Object object) {
	        return 0.2f;
	    }
	    
	

}
