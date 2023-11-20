package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class OnItemUseTicked implements IEntityData {
	public final LivingEntity entity;
	public final ItemStack itemStack;
	public final int maxDuration;
	public final int original;
	public int duration;

	public static Context< OnItemUseTicked > listen( Consumer< OnItemUseTicked > consumer ) {
		return Contexts.get( OnItemUseTicked.class ).add( consumer );
	}

	public OnItemUseTicked( LivingEntity entity, ItemStack itemStack, int maxDuration, int duration ) {
		this.entity = entity;
		this.itemStack = itemStack;
		this.maxDuration = maxDuration;
		this.original = duration;
		this.duration = duration;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public int getDuration() {
		return Mth.clamp( this.duration, 0, this.maxDuration );
	}
}
