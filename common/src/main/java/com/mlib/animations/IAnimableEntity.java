package com.mlib.animations;

import com.mlib.MajruszLibrary;
import com.mlib.entity.EntityHelper;
import com.mlib.platform.Side;

public interface IAnimableEntity {
	AnimationsDef getAnimationsDef();

	Animations getAnimations();

	int getId();

	default Animation playAnimation( String name, int trackIdx ) {
		if( Side.isLogicalServer() ) {
			MajruszLibrary.ENTITY_ANIMATION.sendToClients( new EntityHelper.EntityAnimation( this.getId(), name, trackIdx ) );
		}

		return this.getAnimations().add( new Animation( this.getAnimationsDef().animations.get( name ) ), trackIdx );
	}
}
