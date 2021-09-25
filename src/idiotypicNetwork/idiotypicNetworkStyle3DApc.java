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


public class idiotypicNetworkStyle3DApc implements Style3D<AntigenPresentingCell> {

	Texture texture;
	public idiotypicNetworkStyle3DApc () {
		
		super();
		
		TextureLoader loader = new TextureLoader("icons/apc3d.jpg", "RGB", new Container());
    texture = loader.getTexture();
    texture.setBoundaryModeS(Texture.WRAP);
    texture.setBoundaryModeT(Texture.WRAP);
		
	}
	
	@Override
	public TaggedBranchGroup getBranchGroup(AntigenPresentingCell apc, TaggedBranchGroup taggedGroup) {
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
			Sphere sphere = new Sphere(0.04f, primflags,60);

			Shape3D shape = new Shape3D(sphere.getShape().getGeometry(),ap);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
			
			taggedGroup.getBranchGroup().addChild(shape);
			
			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(AntigenPresentingCell apc) {
		return null; 
	}
	public String getLabel(AntigenPresentingCell apc, String currentLabel) {		
		//return moon.getName();
		return null;
	}
	public Color getLabelColor(AntigenPresentingCell apc, Color currentColor) {
		return Color.YELLOW;
	}
	public Font getLabelFont(AntigenPresentingCell apc, Font currentFont) {
		return null;
	}
	public LabelPosition getLabelPosition(AntigenPresentingCell apc, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}
	public float getLabelOffset(AntigenPresentingCell apc) {
		return .75f;
	}
	public TaggedAppearance getAppearance(AntigenPresentingCell apc, TaggedAppearance taggedAppearance, Object shapeID) {
		
//		if (taggedAppearance == null) {
//			taggedAppearance = new TaggedAppearance();
//		}
//		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.blue);
		
		return taggedAppearance;
	}
	public float[] getScale(AntigenPresentingCell apc) {
		return null;
	}

}
