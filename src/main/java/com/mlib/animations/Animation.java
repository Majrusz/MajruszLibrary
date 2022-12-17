package com.mlib.animations;

import org.joml.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/** Class to handle animations and simulate frames between ticks (otherwise animations would have only 20 frames per second). */
public class Animation< Type > {
	final List< Frame< Type > > frames = new ArrayList<>();
	final float step;

	public Animation( int ticks ) {
		this.step = 1.0f / ticks;
	}

	public static void applyRotationInDegrees( Vector3f value, ModelPart modelPart ) {
		value.mul( Mth.DEG_TO_RAD );
		modelPart.setRotation( value.x(), value.y(), value.z() );
	}

	public static void applyPosition( Vector3f value, ModelPart modelPart ) {
		modelPart.setPos( value.x(), value.y(), value.z() );
	}

	public static void addPosition( Vector3f value, ModelPart modelPart ) {
		modelPart.setPos( modelPart.x + value.x(), modelPart.y + value.y(), modelPart.z + value.z() );
	}

	public static void applyScale( float value, ModelPart modelPart ) {
		modelPart.xScale = value;
		modelPart.yScale = value;
		modelPart.zScale = value;
	}

	public Animation< Type > addFrame( Frame< Type > frame ) {
		this.frames.add( frame );

		return this;
	}

	public Type apply( float duration, float ageInTicks ) {
		Frame< Type > currentFrame = null, nextFrame = null;
		for( int i = 0; i < this.frames.size(); ++i ) {
			Frame< Type > frame = this.frames.get( i );
			if( duration > frame.startDuration ) {
				continue;
			}

			currentFrame = this.frames.get( Math.max( i - 1, 0 ) );
			nextFrame = frame;
			break;
		}
		if( currentFrame == null ) {
			return this.frames.get( this.frames.size() - 1 ).getValue();
		}

		return interpolate( duration + ( ageInTicks % 1.0f ) * this.step, currentFrame, nextFrame );
	}

	private Type interpolate( float duration, Frame< Type > currentFrame, Frame< Type > nextFrame ) {
		float ratio = ( duration - currentFrame.startDuration ) / ( nextFrame.startDuration - currentFrame.startDuration );
		ratio = nextFrame.interpolationType.apply( Math.min( ratio, 1.0f ) );

		return currentFrame.interpolate( ratio, nextFrame );
	}

	public static class Float extends Animation< java.lang.Float > {
		public Float( int ticks ) {
			super( ticks );
		}

		public Animation.Float addFrame( FrameFloat frame ) {
			this.frames.add( frame );

			return this;
		}

		public Animation.Float addNewFloatFrame( float startDuration, float value, Frame.InterpolationType interpolationType ) {
			return addFrame( new FrameFloat( startDuration, value, interpolationType ) );
		}

		public Animation.Float addNewFloatFrame( float startDuration, float value ) {
			return addFrame( new FrameFloat( startDuration, value ) );
		}
	}

	public static class Degrees extends Animation.Float {
		public Degrees( int ticks ) {
			super( ticks );
		}

		@Override
		public java.lang.Float apply( float duration, float ageInTicks ) {
			return Mth.DEG_TO_RAD * super.apply( duration, ageInTicks );
		}
	}

	public static class Vector extends Animation< Vector3f > {
		public Vector( int ticks ) {
			super( ticks );
		}

		public Animation.Vector addFrame( FrameVector frame ) {
			this.frames.add( frame );

			return this;
		}

		public Animation.Vector addNewVectorFrame( float startDuration, Vector3f value, Frame.InterpolationType interpolationType ) {
			return addFrame( new FrameVector( startDuration, value, interpolationType ) );
		}

		public Animation.Vector addNewVectorFrame( float startDuration, Vector3f value ) {
			return addFrame( new FrameVector( startDuration, value ) );
		}
	}
}
