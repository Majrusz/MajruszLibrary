package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnPreDamaged {
	public static final Consumer< Data > CANCEL = data->data.isCancelled = true;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( DamageSource source, LivingEntity target, float damage ) {
		return Contexts.get( Data.class ).dispatch( new Data( source, target, damage ) );
	}

	public static Condition< Data > isDirect() {
		return new Condition< Data >( data->data.source.getDirectEntity() == data.attacker );
	}

	public static Condition< Data > dealtAnyDamage() {
		return new Condition< Data >( data->data.damage > 0.0f );
	}

	public static Condition< Data > willTakeFullDamage() {
		return new Condition< Data >( data->data.target.invulnerableTime <= 10 ); // sources like fire deal damage every tick and only invulnerableTime blocks them from applying damage
	}

	public static class Data implements IEntityData {
		public final DamageSource source;
		@Nullable public final LivingEntity attacker;
		public final LivingEntity target;
		public final float damage;
		public float extraDamage = 0;
		public boolean isCancelled = false;
		public boolean spawnCriticalParticles = false;
		public boolean spawnMagicParticles = false;

		public Data( DamageSource source, LivingEntity target, float damage ) {
			this.source = source;
			this.attacker = Utility.castIfPossible( LivingEntity.class, this.source.getEntity() );
			this.target = target;
			this.damage = damage;
		}

		@Override
		public Entity getEntity() {
			return this.target;
		}
	}
}