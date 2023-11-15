package com.mlib.animations;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import com.mlib.time.TimeHelper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation {
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

			Map.Entry< Float, AnimationsDef.RotationDef > current = bone.rotations.floorEntry( duration );
			Map.Entry< Float, AnimationsDef.RotationDef > next = bone.rotations.ceilingEntry( duration );
			if( current.getValue() != next.getValue() ) {
				float ratio = ( float )( duration + ( ageInTicks % 1.0f ) * TimeHelper.toSeconds( 1 ) - current.getKey() ) / ( next.getKey() - current.getKey() );
				ratio = next.getValue().easing.apply( ratio );

				modelPart.xRot += Mth.lerp( ratio, current.getValue().rotation.x, next.getValue().rotation.x );
				modelPart.yRot += Mth.lerp( ratio, current.getValue().rotation.y, next.getValue().rotation.y );
				modelPart.zRot += Mth.lerp( ratio, current.getValue().rotation.z, next.getValue().rotation.z );
			} else {
				modelPart.xRot += current.getValue().rotation.x;
				modelPart.yRot += current.getValue().rotation.y;
				modelPart.zRot += current.getValue().rotation.z;
			}
		} );
	}

	public void addCallback( int tick, Runnable callback ) {
		this.callbacks.computeIfAbsent( tick, ArrayList::new ).add( callback );
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

	Animation( AnimationsDef.AnimationDef def ) {
		this.def = def;
		this.ticksLeft = def.ticks;
	}
}
