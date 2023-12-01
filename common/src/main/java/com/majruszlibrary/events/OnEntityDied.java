package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ICancellableEvent;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntityDied implements ICancellableEvent, IEntityEvent {
	public final DamageSource source;
	public final @Nullable LivingEntity attacker;
	public final LivingEntity target;
	private boolean isDeathCancelled = false;

	public static Event< OnEntityDied > listen( Consumer< OnEntityDied > consumer ) {
		return Events.get( OnEntityDied.class ).add( consumer );
	}

	public OnEntityDied( DamageSource source, LivingEntity target ) {
		this.source = source;
		this.attacker = this.source.getEntity() instanceof LivingEntity entity ? entity : null;
		this.target = target;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isDeathCancelled();
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public boolean isDirect() {
		return this.source.getDirectEntity() == this.attacker;
	}

	public void cancelDeath() {
		this.isDeathCancelled = true;
	}

	public boolean isDeathCancelled() {
		return this.isDeathCancelled;
	}
}
