package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
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
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final LivingHurtEvent event;
		public final DamageSource source;
		@Nullable
		public final LivingEntity attacker;
		public final LivingEntity target;

		Data( Context context, LivingHurtEvent event ) {
			super( context, event.getEntityLiving() );
			this.event = event;
			this.source = event.getSource();
			this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
			this.target = event.getEntityLiving();
		}
	}

	public static class DirectDamage extends Condition.Context< Data > {
		public DirectDamage() {
			super( Data.class, data->data.source.getDirectEntity() == data.attacker );
		}
	}
}
