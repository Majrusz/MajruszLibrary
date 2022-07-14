package com.mlib.gamemodifiers;

import com.mlib.Utility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

public abstract class ContextData {
	@Nullable public final LivingEntity entity;
	@Nullable public final ServerLevel level;

	public ContextData( @Nullable LivingEntity entity ) {
		this.entity = entity;
		this.level = this.entity != null ? Utility.castIfPossible( ServerLevel.class, this.entity.level ) : null;
	}

	public static class Event< EventType extends net.minecraftforge.eventbus.api.Event > extends ContextData {
		public final EventType event;

		public Event( @Nullable LivingEntity entity, EventType event ) {
			super( entity );
			this.event = event;
		}
	}
}
