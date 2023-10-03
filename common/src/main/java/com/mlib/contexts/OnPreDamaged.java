package com.mlib.contexts;

import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnPreDamaged {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( DamageSource source, LivingEntity target, float damage ) {
		return Contexts.get( Data.class ).dispatch( new Data( source, target, damage ) );
	}

	public static Condition< Data > isDirect() {
		return Condition.predicate( Data::isDirect );
	}

	public static Condition< Data > willTakeFullDamage() {
		return Condition.predicate( Data::willTakeFullDamage ); // sources like fire deal damage every tick and only invulnerableTime blocks them from applying damage
	}

	public static class Data implements IEntityData {
		public final DamageSource source;
		public final LivingEntity attacker;
		public final LivingEntity target;
		public final float damage;
		public float extraDamage = 0;
		public boolean spawnCriticalParticles = false;
		public boolean spawnMagicParticles = false;
		private boolean isCancelled = false;

		public Data( DamageSource source, LivingEntity target, float damage ) {
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
			return this.target.invulnerableTime <= 10;
		}
	}
}