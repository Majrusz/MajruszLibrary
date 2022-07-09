package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnDeathData;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDeathContext extends Context< OnDeathData > {
	static final List< OnDeathContext > CONTEXTS = new ArrayList<>();

	public OnDeathContext( Consumer< OnDeathData > consumer, String configName, String configComment ) {
		super( OnDeathData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnDeathContext( Consumer< OnDeathData > consumer ) {
		this( consumer, "OnDeath", "" );
	}

	@SubscribeEvent
	public static void onDamaged( LivingDeathEvent event ) {
		Context.accept( CONTEXTS, new OnDeathData( event ) );
	}
}
