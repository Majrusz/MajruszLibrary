package com.mlib.contexts;

import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnDamaged {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( DamageSource source, LivingEntity target, float damage ) {
		return Contexts.get( Data.class ).dispatch( new Data( source, target, damage ) );
	}

	public static Condition< Data > isDirect() {
		return Condition.predicate( Data::isDirect );
	}

	public static class Data implements IEntityData {
		public final DamageSource source;
		public final LivingEntity attacker;
		public final LivingEntity target;
		public final float damage;

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

		public boolean isDirect() {
			return this.source.getDirectEntity() == this.attacker;
		}
	}
}
