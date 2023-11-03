package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityDamageBlocked implements IEntityData {
	public final DamageSource source;
	public final LivingEntity attacker;
	public final LivingEntity target;

	public static Context< OnEntityDamageBlocked > listen( Consumer< OnEntityDamageBlocked > consumer ) {
		return Contexts.get( OnEntityDamageBlocked.class ).add( consumer );
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
}