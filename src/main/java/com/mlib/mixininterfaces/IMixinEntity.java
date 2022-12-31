package com.mlib.mixininterfaces;

import com.mlib.Utility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;

import java.util.function.BiConsumer;

public interface IMixinEntity {
	static void addGlowTicks( Entity entity, int ticks ) {
		IMixinEntity mixinEntity = Utility.castIfPossible( IMixinEntity.class, entity );
		if( mixinEntity != null ) {
			mixinEntity.addGlowTicks( ticks );
		}
	}

	default void updateListeners( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer ) {}

	void addGlowTicks( int ticks );
}
