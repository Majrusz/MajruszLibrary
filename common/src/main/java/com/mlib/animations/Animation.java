package com.mlib.animations;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.time.TimeHelper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.*;

public class Animation {
	public static Animation INVALID = new Animation( null );
	private final AnimationsDef.AnimationDef def;
	private final Map< Integer, List< Runnable > > callbacks = new HashMap<>();
	private int ticksLeft;

	public static Vector3f to3d( List< Float > values ) {
		return new Vector3f( values.get( 0 ), values.get( 1 ), values.get( 2 ) );
	}

	public static List< Float > to3d( Vector3f vector ) {
		return List.of( vector.x, vector.y, vector.z );
	}

	public static Vector3f toRadians3d( List< Float > values ) {
		float scale = ( float )Math.PI / 180.0f;
		return new Vector3f( scale * values.get( 0 ), scale * values.get( 1 ), scale * values.get( 2 ) );
	}

	public static List< Float > toRadians3d( Vector3f vector ) {
		float scale = 180.0f * ( float )Math.PI;
		return List.of( scale * vector.x, scale * vector.y, scale * vector.z );
	}

	public static Vector2i to2d( List< Integer > values ) {
		return new Vector2i( values.get( 0 ), values.get( 1 ) );
	}

	public static List< Integer > to2d( Vector2i vector ) {
		return List.of( vector.x, vector.y );
	}

	@OnlyIn( Dist.CLIENT )
	public void apply( ModelParts modelParts, float ageInTicks ) {
		float duration = ( float )TimeHelper.toSeconds( Mth.clamp( this.def.ticks - this.ticksLeft, 0, this.def.ticks ) );

		this.def.bones.forEach( ( name, bone )->{
			ModelPart modelPart = modelParts.get( name );

			Vector3f rotation = this.lerp( bone.rotations, duration, ageInTicks );
			modelPart.xRot += rotation.x;
			modelPart.yRot += rotation.y;
			modelPart.zRot += rotation.z;

			Vector3f position = this.lerp( bone.positions, duration, ageInTicks );
			modelPart.x += position.x;
			modelPart.y -= position.y;
			modelPart.z += position.z;

			Vector3f scale = this.lerp( bone.scales, duration, ageInTicks );
			modelPart.xScale *= scale.x;
			modelPart.yScale *= scale.y;
			modelPart.zScale *= scale.z;
		} );
	}

	public Animation addCallback( int tick, Runnable callback ) {
		this.callbacks.computeIfAbsent( tick, ArrayList::new ).add( callback );

		return this;
	}

	public void tick() {
		this.ticksLeft--;

		int tickIdx = this.def.ticks - this.ticksLeft;
		if( this.callbacks.containsKey( tickIdx ) ) {
			this.callbacks.get( tickIdx ).forEach( Runnable::run );
		}

		if( this.isFinished() && this.def.isLooped ) {
			this.ticksLeft = this.def.ticks;
		}
	}

	public boolean isFinished() {
		return this.ticksLeft <= 0;
	}

	@OnlyIn( Dist.CLIENT )
	private Vector3f lerp( TreeMap< Float, ? extends AnimationsDef.VectorDef > vectors, float duration, float ageInTicks ) {
		if( vectors.isEmpty() ) {
			return new Vector3f( 0.0f, 0.0f, 0.0f );
		}

		Map.Entry< Float, ? extends AnimationsDef.VectorDef > current = vectors.floorEntry( duration );
		Map.Entry< Float, ? extends AnimationsDef.VectorDef > next = vectors.ceilingEntry( duration );
		if( current == null ) {
			current = next;
		} else if( next == null ) {
			next = current;
		}

		if( current.getValue() != next.getValue() ) {
			float ratio = ( float )( duration + ( ageInTicks % 1.0f ) * TimeHelper.toSeconds( 1 ) - current.getKey() ) / ( next.getKey() - current.getKey() );
			ratio = next.getValue().easing.apply( ratio );

			return new Vector3f(
				Mth.lerp( ratio, current.getValue().vector.x, next.getValue().vector.x ),
				Mth.lerp( ratio, current.getValue().vector.y, next.getValue().vector.y ),
				Mth.lerp( ratio, current.getValue().vector.z, next.getValue().vector.z )
			);
		} else {
			return new Vector3f(
				current.getValue().vector.x,
				current.getValue().vector.y,
				current.getValue().vector.z
			);
		}
	}

	Animation( AnimationsDef.AnimationDef def ) {
		this.def = def;
		this.ticksLeft = def != null ? def.ticks : 1;
	}
}
