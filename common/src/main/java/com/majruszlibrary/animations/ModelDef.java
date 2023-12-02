package com.majruszlibrary.animations;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.data.Reader;
import com.majruszlibrary.data.Serializables;
import com.majruszlibrary.math.AnyPos;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelDef {
	public List< GeometryDef > geometries = new ArrayList<>();

	static {
		Serializables.get( ModelDef.class )
			.define( "minecraft:geometry", Reader.list( Reader.custom( GeometryDef::new ) ), s->s.geometries, ( s, v )->s.geometries = v );

		Serializables.get( GeometryDef.class )
			.define( "description", Reader.custom( DescriptionDef::new ), s->s.description, ( s, v )->s.description = v )
			.define( "bones", Reader.list( Reader.custom( BoneDef::new ) ), s->s.bones, ( s, v )->s.bones = v );

		Serializables.get( DescriptionDef.class )
			.define( "texture_width", Reader.integer(), s->s.width, ( s, v )->s.width = v )
			.define( "texture_height", Reader.integer(), s->s.height, ( s, v )->s.height = v );

		Serializables.get( BoneDef.class )
			.define( "name", Reader.string(), s->s.name, ( s, v )->s.name = v )
			.define( "parent", Reader.string(), s->s.parent, ( s, v )->s.parent = v )
			.define( "pivot", Reader.list( Reader.number() ), s->Animation.to3d( s.globalPivot ), ( s, v )->s.globalPivot = Animation.to3d( v ) )
			.define( "rotation", Reader.list( Reader.number() ), s->Animation.to3d( s.rotation ), ( s, v )->s.rotation = Animation.to3d( v ) )
			.define( "cubes", Reader.list( Reader.custom( CubeDef::new ) ), s->s.cubes, ( s, v )->s.cubes = v );

		Serializables.get( CubeDef.class )
			.define( "origin", Reader.list( Reader.number() ), s->Animation.to3d( s.origin ), ( s, v )->s.origin = Animation.to3d( v ) )
			.define( "size", Reader.list( Reader.number() ), s->Animation.to3d( s.size ), ( s, v )->s.size = Animation.to3d( v ) )
			.define( "inflate", Reader.number(), s->s.inflate, ( s, v )->s.inflate = v )
			.define( "uv", Reader.list( Reader.integer() ), s->Animation.to2d( s.uv ), ( s, v )->s.uv = Animation.to2d( v ) );
	}

	@OnlyIn( Dist.CLIENT )
	public LayerDefinition toLayerDefinition() {
		GeometryDef geometry = this.geometries.get( 0 );
		MeshDefinition mesh = new MeshDefinition();
		Map< String, Vector3f > offsets = new HashMap<>();
		Map< String, PartDefinition > parts = new HashMap<>();
		for( BoneDef bone : geometry.bones ) {
			CubeListBuilder cubes = CubeListBuilder.create();
			for( CubeDef cube : bone.cubes ) {
				Vector3f position = AnyPos.from( cube.origin ).sub( bone.globalPivot ).add( 0.0f, cube.size.y, 0.0f ).mul( 1.0f, -1.0f, 1.0f ).vec3f();
				cubes.texOffs( cube.uv.x, cube.uv.y )
					.addBox( position.x, position.y, position.z, cube.size.x, cube.size.y, cube.size.z, new CubeDeformation( cube.inflate ) );
			}

			if( bone.parent != null ) {
				bone.pivot = AnyPos.from( bone.globalPivot ).sub( offsets.get( bone.parent ) ).mul( 1.0f, -1.0f, 1.0f ).vec3f();
			} else {
				bone.pivot = AnyPos.from( bone.globalPivot ).mul( 1.0f, -1.0f, 1.0f ).add( 0.0, 24.0, 0.0 ).vec3f();
			}

			PartPose pose;
			if( bone.rotation != null ) {
				float toRadiansScale = ( float )Math.PI / 180.0f;
				pose = PartPose.offsetAndRotation( bone.pivot.x, bone.pivot.y, bone.pivot.z, bone.rotation.x * toRadiansScale, bone.rotation.y * toRadiansScale, bone.rotation.z * toRadiansScale );
			} else {
				pose = PartPose.offset( bone.pivot.x, bone.pivot.y, bone.pivot.z );
			}

			offsets.put( bone.name, bone.globalPivot );
			parts.put( bone.name, ( bone.parent != null ? parts.get( bone.parent ) : mesh.getRoot() ).addOrReplaceChild( bone.name, cubes, pose ) );
		}

		return LayerDefinition.create( mesh, geometry.description.width, geometry.description.height );
	}

	public static class GeometryDef {
		public DescriptionDef description = new DescriptionDef();
		public List< BoneDef > bones = new ArrayList<>();
	}

	public static class DescriptionDef {
		public int width = 32;
		public int height = 32;
	}

	public static class BoneDef {
		public String name = "body";
		public String parent = null;
		public Vector3f globalPivot = new Vector3f( 0.0f, 0.0f, 0.0f );
		public Vector3f pivot = new Vector3f( 0.0f, 0.0f, 0.0f );
		public Vector3f rotation = null;
		public List< CubeDef > cubes = new ArrayList<>();
	}

	public static class CubeDef {
		public Vector3f origin = new Vector3f( 0.0f, 0.0f, 0.0f );
		public Vector3f size = new Vector3f( 0.0f, 0.0f, 0.0f );
		public Float inflate = 0.0f;
		public Vector2i uv = new Vector2i( 0, 0 );
	}
}
