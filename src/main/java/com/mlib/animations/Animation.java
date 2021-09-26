package com.mlib.animations;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

/** Class for easier handling animations. */
public class Animation< Type > {
	protected final List< Frame< Type > > frames;

	@SafeVarargs
	public Animation( Frame< Type >... frames ) {
		this.frames = Arrays.asList( frames );
	}

	public Type apply( float duration ) {
		Frame< Type > currentFrame = null, nextFrame = null;
		for( int i = 0; i < this.frames.size(); ++i ) {
			Frame< Type > frame = this.frames.get( i );
			if( duration > frame.startDuration ) {
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

		return interpolate( duration, currentFrame, nextFrame );
	}

	protected Type interpolate( float duration, Frame< Type > currentFrame, Frame< Type > nextFrame ) {
		float ratio = ( duration - currentFrame.startDuration ) / ( nextFrame.startDuration - currentFrame.startDuration );
		ratio = nextFrame.interpolationType.apply( ratio );

		return currentFrame.interpolate( ratio, nextFrame );
	}

	public static void applyRotationInDegrees( Vec3 value, ModelPart modelPart ) {
		modelPart.setRotation( ( float )( Mth.DEG_TO_RAD * value.x ), ( float )( Mth.DEG_TO_RAD * value.y ), ( float )( Mth.DEG_TO_RAD * value.z ) );
	}

	public static void applyPosition( Vec3 value, ModelPart modelPart ) {
		modelPart.setPos( ( float )( value.x ), ( float )( value.y ), ( float )( value.z ) );
	}
}
