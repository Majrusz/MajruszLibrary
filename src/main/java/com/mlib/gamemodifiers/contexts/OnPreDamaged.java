package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnPreDamaged {
	public static final Consumer< Data > CANCEL = data->data.isCancelled = true;
	public static final Predicate< Data > DIRECT_DAMAGE = data->data.source.getDirectEntity() == data.attacker;
	public static final Predicate< Data > DEALT_ANY_DAMAGE = data->data.damage > 0.0f;
	public static final Predicate< Data > WILL_TAKE_FULL_DAMAGE = data->data.target.invulnerableTime <= 10; // sources like fire deal damage every tick and only invulnerableTime blocks them from applying damage

	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public static Data accept( Data data ) {
			return CONTEXTS.accept( data );
		}

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}
	}

	public static class Data extends ContextData {
		public final DamageSource source;
		@Nullable public final LivingEntity attacker;
		public final LivingEntity target;
		public final float damage;
		public float extraDamage = 0;
		public boolean isCancelled = false;
		public boolean spawnCriticalParticles = false;
		public boolean spawnMagicParticles = false;

		public Data( DamageSource source, LivingEntity target, float damage ) {
			super( target );

			this.source = source;
			this.attacker = Utility.castIfPossible( LivingEntity.class, this.source.getEntity() );
			this.target = target;
			this.damage = damage;
		}
	}
}