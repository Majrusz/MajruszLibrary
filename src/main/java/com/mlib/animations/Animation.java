package com.mlib.animations;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/** Class for easier handling animations. */
public class Animation< Type > {
	protected static final float FRAME_STEP = 0.666666666f;
	protected final List< Frame< Type > > frames = new ArrayList<>();
	protected float lastDuration = 0.0f;
	protected float delta = 0.0f;
	protected int extraTicks = 0;

	public static void applyRotationInDegrees( Vec3 value, ModelPart modelPart ) {
		modelPart.setRotation( ( float )( Mth.DEG_TO_RAD * value.x ), ( float )( Mth.DEG_TO_RAD * value.y ), ( float )( Mth.DEG_TO_RAD * value.z ) );
	}

	public static void applyPosition( Vec3 value, ModelPart modelPart ) {
		modelPart.setPos( ( float )( value.x ), ( float )( value.y ), ( float )( value.z ) );
	}

	public Animation< Type > addFrame( Frame< Type > frame ) {
		this.frames.add( frame );

		return this;
	}

	public Type apply( float duration ) {
		if( duration != this.lastDuration ) {
			this.delta = duration > this.lastDuration ? duration - this.lastDuration : 0.0f;
			this.lastDuration = duration;
			this.extraTicks = 0;
		} else {
			++this.extraTicks;
		}
		// simulates frames between because otherwise animations would have only 20 frames per second
		float newDuration = duration + this.delta * ( 1.0f - ( float )Math.pow( FRAME_STEP, this.extraTicks ) );

		Frame< Type > currentFrame = null, nextFrame = null;
		for( int i = 0; i < this.frames.size(); ++i ) {
			Frame< Type > frame = this.frames.get( i );
			if( newDuration > frame.startDuration ) {
				if( i == this.frames.size() - 1 )
					return frame.getValue();

				continue;
			}

			if( i == 0 )
				return frame.getValue();

			currentFrame = this.frames.get( Math.max( i - 1, 0 ) );
			nextFrame = this.frames.get( i );
			break;
		}

		if( currentFrame == null || nextFrame == null )
			throw new InvalidParameterException( "Animation does not have valid frames" );

		return interpolate( newDuration, currentFrame, nextFrame );
	}

	protected Type interpolate( float duration, Frame< Type > currentFrame, Frame< Type > nextFrame ) {
		float ratio = ( duration - currentFrame.startDuration ) / ( nextFrame.startDuration - currentFrame.startDuration );
		ratio = nextFrame.interpolationType.apply( ratio );

		return currentFrame.interpolate( ratio, nextFrame );
	}
}
