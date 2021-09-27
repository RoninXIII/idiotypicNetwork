package idiotypicNetwork;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.TextureAttributes;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;
import org.jogamp.java3d.utils.geometry.GeometryServiceImpl;


import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;



public class idiotypicNetworkStyle3DAntigen implements Style3D<Antigen> {

	
	Texture texture;
	public idiotypicNetworkStyle3DAntigen () {
		
		super();
		
		TextureLoader loader = new TextureLoader("icons/virus3d.jpg", "RGB", new Container());
    texture = loader.getTexture();
    texture.setBoundaryModeS(Texture.WRAP);
    texture.setBoundaryModeT(Texture.WRAP);
		
	}
	
	
	public TaggedBranchGroup getBranchGroup(Antigen antigen, TaggedBranchGroup taggedGroup) {
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

	public float[] getRotation(Antigen antigen) {
		return null; 
	}
	public String getLabel(Antigen antigen, String currentLabel) {		
		//return moon.getName();
		return null;
	}
	public Color getLabelColor(Antigen antigen, Color currentColor) {
		return Color.YELLOW;
	}
	public Font getLabelFont(Antigen antigen, Font currentFont) {
		return null;
	}
	public LabelPosition getLabelPosition(Antigen antigen, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}
	public float getLabelOffset(Antigen antigen) {
		return .75f;
	}
	public TaggedAppearance getAppearance(Antigen antigen, TaggedAppearance taggedAppearance, Object shapeID) {
		
//		if (taggedAppearance == null) {
//			taggedAppearance = new TaggedAppearance();
//		}
//		AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.blue);
		
		return taggedAppearance;
	}
	public float[] getScale(Antigen antigen) {
		return null;
	}

}
