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

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;


public class idiotypicNetworkStyle3DAntibody implements Style3D<Antibody> {

	
Texture texture;
	
	public idiotypicNetworkStyle3DAntibody (){
		super();
		
		TextureLoader loader = new TextureLoader("icons/antibody3d.png", "RGB", new Container());
    texture = loader.getTexture();
    texture.setBoundaryModeS(Texture.WRAP);
    texture.setBoundaryModeT(Texture.WRAP);
	}
	
	public TaggedBranchGroup getBranchGroup(Antibody antibody, TaggedBranchGroup taggedGroup) {
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

	public float[] getRotation(Antibody antibody) {
		return null; 
	}
	public String getLabel(Antibody antibody, String currentLabel) {		
		return null;
		//return "Ab"+antibody.antigenId;
	}
	public Color getLabelColor(Antibody antibody, Color currentColor) {
		return Color.YELLOW;
	}
	public Font getLabelFont(Antibody antibody, Font currentFont) {
		return null;
	}
	public LabelPosition getLabelPosition(Antibody antibody, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}
	public float getLabelOffset(Antibody antibody) {
		return .75f;
	}
	public TaggedAppearance getAppearance(Antibody antibody, TaggedAppearance taggedAppearance, Object shapeID) {
		
	/*	if (taggedAppearance == null) {
			taggedAppearance = new TaggedAppearance();
		}
		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.blue);*/
		
		return taggedAppearance;
	}
	public float[] getScale(Antibody antibody) {
		return null;
	}
	
	
	
}
