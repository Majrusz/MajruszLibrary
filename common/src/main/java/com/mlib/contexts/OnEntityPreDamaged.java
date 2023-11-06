package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ICancellableData;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntityPreDamaged implements ICancellableData, IEntityData {
	public final DamageSource source;
	public final @Nullable LivingEntity attacker;
	public final LivingEntity target;
	public final float original;
	public float damage;
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
		this.original = damage;
		this.damage = damage;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isDamageCancelled();
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public void cancelDamage() {
		this.isCancelled = true;
	}

	public boolean isDamageCancelled() {
		return this.isCancelled
			|| this.damage <= 0.0f;
	}

	public boolean isDirect() {
		return !this.source.isIndirect();
	}

	public boolean willTakeFullDamage() {
		return this.target.invulnerableTime <= 10; // sources like fire deal damage every tick and only invulnerableTime blocks them from applying damage
	}
}