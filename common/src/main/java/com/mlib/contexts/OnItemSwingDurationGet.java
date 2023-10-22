package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnItemSwingDurationGet implements IEntityData {
	public final LivingEntity entity;
	public final int original;
	public int duration;

	public static Context< OnItemSwingDurationGet > listen( Consumer< OnItemSwingDurationGet > consumer ) {
		return Contexts.get( OnItemSwingDurationGet.class ).add( consumer );
	}

	public OnItemSwingDurationGet( LivingEntity entity, int duration ) {
		this.entity = entity;
		this.original = duration;
		this.duration = duration;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public int getSwingDuration() {
		return Math.max( this.duration, 2 );
	}
}
