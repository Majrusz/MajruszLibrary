package com.mlib.gamemodifiers;

import com.mlib.Utility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Deprecated( forRemoval = true )
public abstract class ContextData {
	@Nullable public final Entity entity;
	@Nullable public final ServerLevel level;

	public ContextData( @Nullable Entity entity ) {
		this.entity = entity;
		this.level = this.entity != null ? Utility.castIfPossible( ServerLevel.class, this.entity.level ) : null;
	}

	public ContextData( @Nullable ServerLevel level ) {
		this.entity = null;
		this.level = level;
	}

	public static class Event< EventType extends net.minecraftforge.eventbus.api.Event > extends ContextData {
		public final EventType event;

		public Event( @Nullable Entity entity, EventType event ) {
			super( entity );
			this.event = event;
		}

		public Event( ServerLevel level, EventType event ) {
			super( level );
			this.event = event;
		}
	}
}
