package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityEffectCheck implements IEntityData {
	public final MobEffectInstance effectInstance;
	public final MobEffect effect;
	public final LivingEntity entity;
	private boolean isEffectCancelled = false;

	public static Context< OnEntityEffectCheck > listen( Consumer< OnEntityEffectCheck > consumer ) {
		return Contexts.get( OnEntityEffectCheck.class ).add( consumer );
	}

	public OnEntityEffectCheck( MobEffectInstance effectInstance, LivingEntity entity ) {
		this.effectInstance = effectInstance;
		this.effect = effectInstance.getEffect();
		this.entity = entity;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public void cancelEffect() {
		this.isEffectCancelled = true;
	}

	public boolean isEffectCancelled() {
		return this.isEffectCancelled;
	}
}
