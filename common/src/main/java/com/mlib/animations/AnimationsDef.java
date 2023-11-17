package com.mlib.animations;

import com.mlib.collection.CollectionHelper;
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
			.defineCustomMap( "animations", s->s.animations, ( s, v )->s.animations = v, AnimationDef::new );

		Serializables.get( AnimationDef.class )
			.defineBoolean( "loop", s->s.isLooped, ( s, v )->s.isLooped = v )
			.defineFloat( "animation_length", s->( float )TimeHelper.toSeconds( s.ticks ), ( s, v )->s.ticks = TimeHelper.toTicks( v ) )
			.defineCustomMap( "bones", s->s.bones, ( s, v )->s.bones = v, BoneDef::new );

		Serializables.get( BoneDef.class )
			.defineCustomMap( "rotation", BoneDef::getRotations, BoneDef::setRotations, RotationDef::new );

		Serializables.get( RotationDef.class )
			.defineFloatList( "vector", s->Animation.toRadians3d( s.rotation ), ( s, v )->s.rotation = Animation.toRadians3d( v ) )
			.defineEnum( "easing", s->s.easing, ( s, v )->s.easing = v, Easing::values );
	}

	public static class AnimationDef {
		public boolean isLooped = false;
		public int ticks = 20;
		public Map< String, BoneDef > bones = new HashMap<>();
	}

	public static class BoneDef {
		public TreeMap< Float, RotationDef > rotations = new TreeMap<>();

		Map< String, RotationDef > getRotations() {
			return CollectionHelper.mapKey( this.rotations, Object::toString, HashMap::new );
		}

		void setRotations( Map< String, RotationDef > rotations ) {
			this.rotations = CollectionHelper.mapKey( rotations, Float::parseFloat, TreeMap::new );
		}
	}

	public static class RotationDef {
		public Vector3f rotation = new Vector3f( 0.0f, 0.0f, 0.0f );
		public Easing easing = Easing.LINEAR;
	}

	public enum Easing {
		LINEAR( "linear", x->x ),
		QUADRATIC_OUT( "easeOutQuad", x->( float )Math.sqrt( x ) ),
		QUADRATIC_IN( "easeInQuad", x->x * x );

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
