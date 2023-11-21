package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ICancellableEvent;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityEffectCheck implements ICancellableEvent, IEntityEvent {
	public final MobEffectInstance effectInstance;
	public final MobEffect effect;
	public final LivingEntity entity;
	private boolean isEffectCancelled = false;

	public static Event< OnEntityEffectCheck > listen( Consumer< OnEntityEffectCheck > consumer ) {
		return Events.get( OnEntityEffectCheck.class ).add( consumer );
	}

	public OnEntityEffectCheck( MobEffectInstance effectInstance, LivingEntity entity ) {
		this.effectInstance = effectInstance;
		this.effect = effectInstance.getEffect();
		this.entity = entity;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isEffectCancelled();
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
