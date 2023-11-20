package com.majruszlibrary.animations;

import com.majruszlibrary.MajruszLibrary;
import com.majruszlibrary.entity.EntityHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface IAnimableEntity {
	AnimationsDef getAnimationsDef();

	Animations getAnimations();

	int getId();

	Level level();

	default Animation playAnimation( String name, int trackIdx ) {
		if( this.getAnimationsDef() == null ) {
			return Animation.INVALID;
		}

		if( this.level() instanceof ServerLevel ) {
			MajruszLibrary.ENTITY_ANIMATION.sendToClients( new EntityHelper.EntityAnimation( this.getId(), name, trackIdx ) );
		}

		return this.getAnimations().add( new Animation( this.getAnimationsDef().animations.get( name ) ), trackIdx );
	}

	default Animation playAnimation( String name ) {
		return this.playAnimation( name, 0 );
	}
}
