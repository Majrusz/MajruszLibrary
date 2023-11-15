package com.mlib.animations;

import com.mlib.MajruszLibrary;
import com.mlib.entity.EntityHelper;
import com.mlib.platform.Side;

import java.util.List;

public interface IAnimableEntity {
	AnimationsDef getAnimationsDef();

	List< Animation > getAnimations();

	int getId();

	default void tickAnimations() {
		List< Animation > animations = this.getAnimations();
		animations.forEach( Animation::tick );
		animations.removeIf( Animation::isFinished );
	}

	default void playAnimation( String name ) {
		if( Side.isLogicalServer() ) {
			MajruszLibrary.ENTITY_ANIMATION.sendToClients( new EntityHelper.EntityAnimation( this.getId(), name ) );
		}

		this.getAnimations().add( new Animation( this.getAnimationsDef().animations.get( name ) ) );
	}
}
