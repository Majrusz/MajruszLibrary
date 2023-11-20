package com.majruszlibrary.animations;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import net.minecraft.client.model.geom.ModelPart;

import java.util.HashMap;
import java.util.Map;

@OnlyIn( Dist.CLIENT )
public class ModelParts {
	private final ModelPart root;
	private final ModelDef model;
	private final Map< String, ModelPart > parts = new HashMap<>();

	public ModelParts( ModelPart root, ModelDef model ) {
		this.root = root;
		this.model = model;
		for( ModelDef.BoneDef bone : model.geometries.get( 0 ).bones ) {
			this.parts.put( bone.name, ( bone.parent != null ? this.parts.get( bone.parent ) : this.root ).getChild( bone.name ) );
		}
	}

	public void reset() {
		this.model.geometries.get( 0 ).bones.forEach( bone->{
			ModelPart modelPart = this.get( bone.name );
			modelPart.setPos( bone.pivot.x, bone.pivot.y, bone.pivot.z );
			if( bone.rotation != null ) {
				modelPart.setRotation( bone.rotation.x, bone.rotation.y, bone.rotation.z );
			} else {
				modelPart.setRotation( 0.0f, 0.0f, 0.0f );
			}
			modelPart.xScale = modelPart.yScale = modelPart.zScale = 1.0f;
		} );
	}

	public ModelPart get( String name ) {
		return this.parts.get( name );
	}

	public ModelPart getRoot() {
		return this.root;
	}
}
