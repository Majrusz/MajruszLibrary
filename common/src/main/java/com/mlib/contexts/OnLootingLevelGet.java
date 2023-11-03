package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnLootingLevelGet implements IEntityData {
	public final LivingEntity entity;
	public final int original;
	public int level;

	public static Context< OnLootingLevelGet > listen( Consumer< OnLootingLevelGet > consumer ) {
		return Contexts.get( OnLootingLevelGet.class ).add( consumer );
	}

	public OnLootingLevelGet( LivingEntity entity, int level ) {
		this.entity = entity;
		this.original = level;
		this.level = level;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public int getLootingLevel() {
		return Math.max( this.level, 0 );
	}
}
