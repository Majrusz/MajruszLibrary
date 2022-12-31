package com.mlib.mixininterfaces;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;

import java.util.function.BiConsumer;

public interface IMixinEntity {
	default void updateListeners( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer ) {}
}
