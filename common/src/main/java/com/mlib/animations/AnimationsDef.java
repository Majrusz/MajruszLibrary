package com.mlib.animations;

import com.mlib.collection.CollectionHelper;
import com.mlib.data.Reader;
import com.mlib.data.Serializables;
import com.mlib.time.TimeHelper;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class AnimationsDef {
	public Map< String, AnimationDef > animations = new HashMap<>();

	static {
		Serializables.get( AnimationsDef.class )
			.define( "animations", Reader.map( Reader.custom( AnimationDef::new ) ), s->s.animations, ( s, v )->s.animations = v );

		Serializables.get( AnimationDef.class )
			.define( "loop", Reader.bool(), s->s.isLooped, ( s, v )->s.isLooped = v )
			.define( "animation_length", Reader.number(), s->( float )TimeHelper.toSeconds( s.ticks ), ( s, v )->s.ticks = TimeHelper.toTicks( v ) )
			.define( "bones", Reader.map( Reader.custom( BoneDef::new ) ), s->s.bones, ( s, v )->s.bones = v );

		Serializables.get( BoneDef.class )
			.define( "rotation", Reader.map( Reader.custom( RotationDef::new ) ), BoneDef::getRotations, BoneDef::setRotations )
			.define( "position", Reader.map( Reader.custom( VectorDef::new ) ), BoneDef::getPositions, BoneDef::setPositions )
			.define( "scale", Reader.map( Reader.custom( VectorDef::new ) ), BoneDef::getScales, BoneDef::setScales );

		Serializables.get( RotationDef.class )
			.define( "vector", Reader.list( Reader.number() ), s->Animation.toRadians3d( s.vector ), ( s, v )->s.vector = Animation.toRadians3d( v ) )
			.define( "easing", Reader.enumeration( Easing::values ), s->s.easing, ( s, v )->s.easing = v );

		Serializables.get( VectorDef.class )
			.define( "vector", Reader.list( Reader.number() ), s->Animation.to3d( s.vector ), ( s, v )->s.vector = Animation.to3d( v ) )
			.define( "easing", Reader.enumeration( Easing::values ), s->s.easing, ( s, v )->s.easing = v );
	}

	public static class AnimationDef {
		public boolean isLooped = false;
		public int ticks = 20;
		public Map< String, BoneDef > bones = new HashMap<>();
	}

	public static class BoneDef {
		public TreeMap< Float, RotationDef > rotations = new TreeMap<>();
		public TreeMap< Float, VectorDef > positions = new TreeMap<>();
		public TreeMap< Float, VectorDef > scales = new TreeMap<>();

		void setRotations( Map< String, RotationDef > rotations ) {
			this.rotations = CollectionHelper.mapKey( rotations, Float::parseFloat, TreeMap::new );
		}

		void setPositions( Map< String, VectorDef > rotations ) {
			this.positions = CollectionHelper.mapKey( rotations, Float::parseFloat, TreeMap::new );
		}

		void setScales( Map< String, VectorDef > rotations ) {
			this.scales = CollectionHelper.mapKey( rotations, Float::parseFloat, TreeMap::new );
		}

		Map< String, RotationDef > getRotations() {
			return CollectionHelper.mapKey( this.rotations, Object::toString, HashMap::new );
		}

		Map< String, VectorDef > getPositions() {
			return CollectionHelper.mapKey( this.positions, Object::toString, HashMap::new );
		}

		Map< String, VectorDef > getScales() {
			return CollectionHelper.mapKey( this.scales, Object::toString, HashMap::new );
		}
	}

	public static class RotationDef extends VectorDef {}

	public static class VectorDef {
		public Vector3f vector = new Vector3f( 0.0f, 0.0f, 0.0f );
		public Easing easing = Easing.LINEAR;
	}

	public enum Easing {
		LINEAR( "linear", x->x ),
		EASEOUTQUAD( "easeOutQuad", x->( float )Math.sqrt( x ) ),
		EASEINQUAD( "easeInQuad", x->x * x );

		final String id;
		final Function< Float, Float > mapper;

		Easing( String id, Function< Float, Float > mapper ) {
			this.id = id;
			this.mapper = mapper;
		}

		@Override
		public String toString() {
			return this.id;
		}

		public float apply( float ratio ) {
			return this.mapper.apply( ratio );
		}
	}
}
