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


/**
 * @author Mario
 *
 */
public class idiotypicNetworkStyle3DMemoryCell implements Style3D<MemoryKeeperCell>{

	Texture texture;
	public idiotypicNetworkStyle3DMemoryCell () {
		
		super();
		
		TextureLoader loader = new TextureLoader("icons/moon.jpg", "RGB", new Container());
    texture = loader.getTexture();
    texture.setBoundaryModeS(Texture.WRAP);
    texture.setBoundaryModeT(Texture.WRAP);
		
	}
	
	@Override
	public TaggedBranchGroup getBranchGroup(MemoryKeeperCell memoryKeeperCell, TaggedBranchGroup taggedGroup) {
		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
						
			TextureAttributes texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.MODULATE);
	    Appearance ap = new Appearance();
	    ap.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	    ap.setCapability(Appearance.ALLOW_TEXTURE_READ);
	    ap.setTexture(texture);
	    ap.setTextureAttributes(texAttr);
			
			int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
			Sphere sphere = new Sphere(0.01f, primflags,60);

			Shape3D shape = new Shape3D(sphere.getShape().getGeometry(),ap);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
			
			taggedGroup.getBranchGroup().addChild(shape);
			
			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(MemoryKeeperCell memoryKeeperCell) {
		return null; 
	}
	public String getLabel(MemoryKeeperCell memoryKeeperCell, String currentLabel) {		
		//return moon.getName();
		return null;
	}
	public Color getLabelColor(MemoryKeeperCell memoryKeeperCell, Color currentColor) {
		return Color.YELLOW;
	}
	public Font getLabelFont(MemoryKeeperCell memoryKeeperCell, Font currentFont) {
		return null;
	}
	public LabelPosition getLabelPosition(MemoryKeeperCell memoryKeeperCell, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}
	public float getLabelOffset(MemoryKeeperCell memoryKeeperCell) {
		return .75f;
	}
	public TaggedAppearance getAppearance(MemoryKeeperCell memoryKeeperCell, TaggedAppearance taggedAppearance, Object shapeID) {
		
//		if (taggedAppearance == null) {
//			taggedAppearance = new TaggedAppearance();
//		}
//		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.blue);
		
		return taggedAppearance;
	}
	public float[] getScale(MemoryKeeperCell memoryKeeperCell) {
		return null;
	}
}
