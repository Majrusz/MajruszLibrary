package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityPreDamaged implements IEntityData {
	public final DamageSource source;
	public final LivingEntity attacker;
	public final LivingEntity target;
	public final float damage;
	public float extraDamage = 0;
	public boolean spawnCriticalParticles = false;
	public boolean spawnMagicParticles = false;
	private boolean isCancelled = false;

	public static Context< OnEntityPreDamaged > listen( Consumer< OnEntityPreDamaged > consumer ) {
		return Contexts.get( OnEntityPreDamaged.class ).add( consumer );
	}

	public OnEntityPreDamaged( DamageSource source, LivingEntity target, float damage ) {
		this.source = source;
		this.attacker = this.source.getEntity() instanceof LivingEntity entity ? entity : null;
		this.target = target;
		this.damage = damage;
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public void cancelDamage() {
		this.isCancelled = true;
	}

	public boolean isDamageCancelled() {
		return this.isCancelled;
	}

	public boolean isDirect() {
		return this.source.getDirectEntity() == this.attacker;
	}

	public boolean willTakeFullDamage() {
		return this.target.invulnerableTime <= 10; // sources like fire deal damage every tick and only invulnerableTime blocks them from applying damage
	}
}