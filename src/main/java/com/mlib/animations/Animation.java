package com.mlib.animations;

import com.mlib.Utility;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiConsumer;

/** Class to handle animations and simulate frames between ticks (otherwise animations would have only 20 frames per second). */
public class Animation< Type > {
	final TreeMap< Float, Frame< Type > > frames = new TreeMap<>();
	final float step;

	public Animation( int ticks ) {
		this.step = 1.0f / ticks;
	}

	public Animation( double seconds ) {
		this( Utility.secondsToTicks( seconds ) );
	}

	public static void applyRotationInDegrees( Vector3f value, ModelPart... modelParts ) {
		for( ModelPart modelPart : modelParts ) {
			value.mul( Mth.DEG_TO_RAD );
			modelPart.setRotation( value.x(), value.y(), value.z() );
		}
	}

	public static void applyPosition( Vector3f value, ModelPart... modelParts ) {
		for( ModelPart modelPart : modelParts ) {
			modelPart.setPos( value.x(), value.y(), value.z() );
		}
	}

	public static void addPosition( Vector3f value, ModelPart... modelParts ) {
		for( ModelPart modelPart : modelParts ) {
			modelPart.setPos( modelPart.x + value.x(), modelPart.y + value.y(), modelPart.z + value.z() );
		}
	}

	public static void applyScale( float value, ModelPart... modelParts ) {
		/*for( ModelPart modelPart : modelParts ) {
			modelPart.xScale = value;
			modelPart.yScale = value;
			modelPart.zScale = value;
		}*/
	}

	public Animation< Type > add( float duration, Frame< Type > frame ) {
		this.frames.put( duration, frame );

		return this;
	}

	public Type apply( float duration, float ageInTicks ) {
		duration = Mth.clamp( duration, this.frames.firstKey(), this.frames.lastKey() );
		Map.Entry< Float, Frame< Type > > current = this.frames.floorEntry( duration );
		Map.Entry< Float, Frame< Type > > next = this.frames.ceilingEntry( duration );
		if( !Objects.equals( current.getKey(), next.getKey() ) ) {
			float ratio = ( duration + ( ageInTicks % 1.0f ) * this.step - current.getKey() ) / ( next.getKey() - current.getKey() );

			return current.getValue().interpolate( Math.min( ratio, 1.0f ), next.getValue() );
		} else {
			return next.getValue().getValue();
		}
	}

	public record Applier( float duration, float ageInTicks ) {
		public static Applier setup( float duration, float ageInTicks ) {
			return new Applier( duration, ageInTicks );
		}

		public < Type > Applier apply( Animation< Type > animation, BiConsumer< Type, ModelPart[] > consumer,
			ModelPart... modelParts
		) {
			consumer.accept( animation.apply( this.duration, this.ageInTicks ), modelParts );

			return this;
		}
	}
}
