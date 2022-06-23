package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Condition;
import com.mlib.Utility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnDamagedContext extends Context {
	static final List< OnDamagedContext > CONTEXTS = new ArrayList<>();

	public OnDamagedContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnDamagedContext() {
		this( "OnDamaged", "" );
	}

	@SubscribeEvent
	public static void onDamaged( LivingHurtEvent event ) {
		DamageSource source = event.getSource();
		LivingEntity attacker = source.getEntity() instanceof LivingEntity ? ( LivingEntity )source.getEntity() : null;
		LivingEntity target = event.getEntityLiving();
		Data data = new Data( event, attacker, target, source );

		for( OnDamagedContext context : CONTEXTS ) {
			if( context.check( data ) ) {
				context.gameModifier.execute( data );
			}
		}
	}

	public static class Data extends Context.Data {
		public final LivingHurtEvent event;
		@Nullable
		public final LivingEntity attacker;
		public final LivingEntity target;
		@Nullable
		public final ServerLevel level;
		public final DamageSource source;

		public Data( LivingHurtEvent event, @Nullable LivingEntity attacker, LivingEntity target, DamageSource source ) {
			super( target );
			this.event = event;
			this.attacker = attacker;
			this.target = target;
			this.level = Utility.castIfPossible( ServerLevel.class, this.target.level );
			this.source = source;
		}
	}

	public static class DirectDamage extends Condition.Context< Data > {
		public DirectDamage() {
			super( Data.class, data->data.source.getDirectEntity() == data.attacker );
		}
	}
}
