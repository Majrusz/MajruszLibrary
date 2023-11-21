package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityDamageBlocked implements IEntityEvent {
	public final DamageSource source;
	public final LivingEntity attacker;
	public final LivingEntity target;

	public static Event< OnEntityDamageBlocked > listen( Consumer< OnEntityDamageBlocked > consumer ) {
		return Events.get( OnEntityDamageBlocked.class ).add( consumer );
	}

	public OnEntityDamageBlocked( DamageSource source, LivingEntity target ) {
		this.source = source;
		this.attacker = this.source.getEntity() instanceof LivingEntity entity ? entity : null;
		this.target = target;
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public boolean isDirect() {
		return !this.source.isIndirect();
	}
}