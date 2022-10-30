package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnPreDamaged {
	public static final Predicate< Data > DIRECT_DAMAGE = data->data.source.getDirectEntity() == data.attacker;
	public static final Predicate< Data > DEALT_ANY_DAMAGE = data->data.event.getAmount() > 0.0f;
	public static final Predicate< Data > WILL_TAKE_FULL_DAMAGE = data->data.target.invulnerableTime < 10.0f; // sources like fire deal damage every tick and only invulnerableTime blocks them from applying damage

	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( Data.class, consumer, params );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onPreDamaged( LivingAttackEvent event ) {
			Data data = new Data( event );
			if( !willBeCancelled( data ) ) {
				CONTEXTS.accept( data );
			}
		}

		private static boolean willBeCancelled( Data data ) {
			boolean isInvulnerable = data.target.isInvulnerableTo( data.source );
			boolean isClientSide = data.level == null;
			boolean isDeadOrDying = data.target.isDeadOrDying();
			boolean isFireResistant = data.source.isFire() && data.target.hasEffect( MobEffects.FIRE_RESISTANCE );

			return isInvulnerable || isClientSide || isDeadOrDying || isFireResistant;
		}
	}

	public static class Data extends ContextData.Event< LivingAttackEvent > {
		public final DamageSource source;
		@Nullable public final LivingEntity attacker;
		public final LivingEntity target;

		public Data( LivingAttackEvent event ) {
			super( event.getEntity(), event );
			this.source = event.getSource();
			this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
			this.target = event.getEntity();
		}
	}
}