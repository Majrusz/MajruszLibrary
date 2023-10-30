package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntitySwimSpeedMultiplierGet implements IEntityData {
	public final LivingEntity entity;
	public final float original;
	public float multiplier;

	public static Context< OnEntitySwimSpeedMultiplierGet > listen( Consumer< OnEntitySwimSpeedMultiplierGet > consumer ) {
		return Contexts.get( OnEntitySwimSpeedMultiplierGet.class ).add( consumer );
	}

	public OnEntitySwimSpeedMultiplierGet( LivingEntity entity, float multiplier ) {
		this.entity = entity;
		this.original = multiplier;
		this.multiplier = multiplier;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public float getMultiplier() {
		return Math.max( 0.0f, this.multiplier );
	}
}
