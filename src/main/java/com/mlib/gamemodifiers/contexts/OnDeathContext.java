package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.Utility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnDeathContext extends Context {
	static final List< OnDeathContext > CONTEXTS = new ArrayList<>();

	public OnDeathContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnDeathContext() {
		this( "OnDeath", "" );
	}

	@SubscribeEvent
	public static void onDamaged( LivingDeathEvent event ) {
		DamageSource source = event.getSource();
		LivingEntity attacker = source.getEntity() instanceof LivingEntity ? ( LivingEntity )source.getEntity() : null;
		LivingEntity target = event.getEntityLiving();
		Data data = new Data( event, attacker, target, source );

		for( OnDeathContext context : CONTEXTS ) {
			if( context.check( data ) ) {
				context.gameModifier.execute( data );
			}
		}
	}

	public static class Data extends Context.Data {
		public final LivingDeathEvent event;
		@Nullable
		public final LivingEntity attacker;
		public final LivingEntity target;
		@Nullable
		public final ServerLevel level;
		public final DamageSource source;

		public Data( LivingDeathEvent event, @Nullable LivingEntity attacker, LivingEntity target, DamageSource source ) {
			super( target );
			this.event = event;
			this.attacker = attacker;
			this.target = target;
			this.level = Utility.castIfPossible( ServerLevel.class, this.target.level );
			this.source = source;
		}
	}
}
