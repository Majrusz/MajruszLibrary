package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
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
