package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnLootingLevelGet implements IEntityEvent {
	public final @Nullable DamageSource source;
	public final LivingEntity entity;
	public final int original;
	public int level;

	public static Event< OnLootingLevelGet > listen( Consumer< OnLootingLevelGet > consumer ) {
		return Events.get( OnLootingLevelGet.class ).add( consumer );
	}

	public OnLootingLevelGet( LivingEntity entity, int level ) {
		this.source = Cache.SOURCE;
		this.entity = entity;
		this.original = level;
		this.level = level;

		Cache.SOURCE = null;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public int getLootingLevel() {
		return Math.max( this.level, 0 );
	}

	public static class Cache {
		public static DamageSource SOURCE = null;
	}
}
