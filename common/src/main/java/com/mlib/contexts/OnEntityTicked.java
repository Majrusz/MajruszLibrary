package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityTicked implements IEntityData {
	public final LivingEntity entity;

	public static Context< OnEntityTicked > listen( Consumer< OnEntityTicked > consumer ) {
		return Contexts.get( OnEntityTicked.class ).add( consumer );
	}

	public OnEntityTicked( LivingEntity entity ) {
		this.entity = entity;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}
}
