package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
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
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final LivingDeathEvent event;
		public final DamageSource source;
		@Nullable
		public final LivingEntity attacker;
		public final LivingEntity target;

		Data( Context context, LivingDeathEvent event ) {
			super( context, event.getEntityLiving() );
			this.event = event;
			this.source = event.getSource();
			this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
			this.target = event.getEntityLiving();
		}
	}
}
