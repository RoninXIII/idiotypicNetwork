/**
 * 
 */
package idiotypicNetwork;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;


import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;
import repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition;

/**
 * @author Mario
 *
 */
public class idiotypicNetworkStyle3DImmuneSystem implements Style3D<immuneSystem> {

	
	Texture texture;
	
	public idiotypicNetworkStyle3DImmuneSystem (){
		super();
		
		TextureLoader loader = new TextureLoader("icons/immuneSystem.jpg", "RGB", new Container());
    texture = loader.getTexture();
    texture.setBoundaryModeS(Texture.WRAP);
    texture.setBoundaryModeT(Texture.WRAP);
	}
	
	
	public TaggedBranchGroup getBranchGroup(immuneSystem o, TaggedBranchGroup taggedGroup) {
		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
						
			TextureAttributes texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.MODULATE);
	    Appearance ap = new Appearance();
	    ap.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	    ap.setCapability(Appearance.ALLOW_TEXTURE_READ);
	    ap.setTexture(texture);
	    ap.setTextureAttributes(texAttr);
			
			int primflags = Primitive.GENERATE_NORMALS_INWARD + Primitive.GENERATE_TEXTURE_COORDS;
			Sphere sphere = new Sphere(5.0f, primflags,60);

			Shape3D shape = new Shape3D(sphere.getShape().getGeometry(),ap);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
			
			taggedGroup.getBranchGroup().addChild(shape);
			
			return taggedGroup;
		}
		return null;
	}
	
	
	

	public float[] getRotation(immuneSystem o) {
		return null; 
	}
	public String getLabel(immuneSystem o, String currentLabel) {		
		return null;
	}
	public Color getLabelColor(immuneSystem o, Color currentColor) {
		return Color.YELLOW;
	}
	public Font getLabelFont(immuneSystem o, Font currentFont) {
		return null;
	}
	public LabelPosition getLabelPosition(immuneSystem o, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}
	public float getLabelOffset(immuneSystem o) {
		return .75f;
	}
	public TaggedAppearance getAppearance(immuneSystem o, TaggedAppearance taggedAppearance, Object shapeID) {
		return taggedAppearance;
	}
	public float[] getScale(immuneSystem o) {
		return null;
	}

	
	
	
}
