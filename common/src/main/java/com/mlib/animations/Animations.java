package com.mlib.animations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Animations {
	private final List< List< Animation > > animations = new ArrayList<>();

	public static Animations create( int trackCount ) {
		return new Animations( trackCount );
	}

	public static Animations create() {
		return new Animations( 1 );
	}

	public Animation add( Animation animation, int trackIdx ) {
		this.animations.get( trackIdx ).add( animation );

		return animation;
	}

	public Animation add( Animation animation ) {
		return this.add( animation, 0 );
	}

	public void tick() {
		for( int trackIdx = 0; trackIdx < this.animations.size(); ++trackIdx ) {
			if( !this.isEmpty( trackIdx ) ) {
				List< Animation > animations = this.animations.get( trackIdx );
				Animation animation = animations.get( 0 );
				animation.tick();
				if( animation.isFinished() ) {
					animations.remove( 0 );
				}
			}
		}
	}

	public void forEach( Consumer< Animation > consumer ) {
		for( int trackIdx = 0; trackIdx < this.animations.size(); ++trackIdx ) {
			if( !this.isEmpty( trackIdx ) ) {
				consumer.accept( this.animations.get( trackIdx ).get( 0 ) );
			}
		}
	}

	public boolean isEmpty( int trackId ) {
		return this.animations.get( trackId ).isEmpty();
	}

	public boolean isEmpty() {
		return this.isEmpty( 0 );
	}

	private Animations( int trackCount ) {
		for( int trackIdx = 0; trackIdx < trackCount; ++trackIdx ) {
			this.animations.add( new ArrayList<>() );
		}
	}
}
