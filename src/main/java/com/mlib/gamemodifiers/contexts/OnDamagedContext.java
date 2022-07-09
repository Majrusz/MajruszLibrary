package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnDamagedData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mod.EventBusSubscriber
public class OnDamagedContext extends Context< OnDamagedData > {
	public static final Predicate< OnDamagedData > DIRECT_DAMAGE = data->data.source.getDirectEntity() == data.attacker;
	static final List< OnDamagedContext > CONTEXTS = new ArrayList<>();

	public OnDamagedContext( Consumer< OnDamagedData > consumer, String configName, String configComment ) {
		super( OnDamagedData.class, consumer, configName, configComment );
		Context.addSorted( CONTEXTS, this );
	}

	public OnDamagedContext( Consumer< OnDamagedData > consumer ) {
		this( consumer, "OnDamaged", "" );
	}

	@SubscribeEvent
	public static void onDamaged( LivingHurtEvent event ) {
		Context.accept( CONTEXTS, new OnDamagedData( event ) );
	}
}
