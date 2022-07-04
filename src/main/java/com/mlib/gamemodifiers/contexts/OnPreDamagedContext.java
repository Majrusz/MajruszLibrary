package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPreDamagedData;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPreDamagedContext extends Context< OnPreDamagedData > {
	static final List< OnPreDamagedContext > CONTEXTS = new ArrayList<>();

	public OnPreDamagedContext( Consumer< OnPreDamagedData > consumer, String configName, String configComment ) {
		super( OnPreDamagedData.class, consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPreDamagedContext( Consumer< OnPreDamagedData > consumer ) {
		this( consumer, "OnPreDamaged", "" );
	}

	@SubscribeEvent
	public static void onPreDamaged( LivingAttackEvent event ) {
		handleContexts( new OnPreDamagedData( event ), CONTEXTS );
	}
}
