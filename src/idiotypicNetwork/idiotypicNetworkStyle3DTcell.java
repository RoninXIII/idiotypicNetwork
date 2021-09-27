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
import org.jogamp.java3d.utils.geometry.GeometryServiceImpl;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;



public class idiotypicNetworkStyle3DTcell implements Style3D<Tcell> {

	Texture texture;
	public idiotypicNetworkStyle3DTcell () {
		
		super();
		
		TextureLoader loader = new TextureLoader("icons/tcell3d.jpg", "RGB", new Container());
    texture = loader.getTexture();
    texture.setBoundaryModeS(Texture.WRAP);
    texture.setBoundaryModeT(Texture.WRAP);
		
	}
	
	
	public TaggedBranchGroup getBranchGroup(Tcell tcell, TaggedBranchGroup taggedGroup) {
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
			Sphere sphere = new Sphere(0.08f, primflags,60);

			Shape3D shape = new Shape3D(sphere.getShape().getGeometry(),ap);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
			
			taggedGroup.getBranchGroup().addChild(shape);
			
			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(Tcell tcell) {
		return null; 
	}
	public String getLabel(Tcell tcell, String currentLabel) {		
		
		/*if (tcell.type2 == "helper" && tcell.type2 == "naive") {
			return "T-helper";
		} else if (tcell.type2 == "helper" && tcell.type2 == "activated") {
			return "Th - "+tcell.antigenId;
		}else return "T-suppressor";*/
		return null;
	}
	public Color getLabelColor(Tcell tcell, Color currentColor) {
		return Color.YELLOW;
	}
	public Font getLabelFont(Tcell tcellc, Font currentFont) {
		return null;
	}
	public LabelPosition getLabelPosition(Tcell tcell, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}
	public float getLabelOffset(Tcell tcell) {
		return .75f;
	}
	public TaggedAppearance getAppearance(Tcell tcell, TaggedAppearance taggedAppearance, Object shapeID) {
		
//		if (taggedAppearance == null) {
//			taggedAppearance = new TaggedAppearance();
//		}
//		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.blue);
		
		return taggedAppearance;
	}
	public float[] getScale(Tcell tcell) {
		return null;
	}
	
}
