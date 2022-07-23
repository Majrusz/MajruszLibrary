package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ProjectileEvent;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnProjectileData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnProjectileHitContext extends Context< OnProjectileData.Hit > {
	static final List< OnProjectileHitContext > CONTEXTS = new ArrayList<>();

	public OnProjectileHitContext( Consumer< OnProjectileData.Hit > consumer, ContextParameters params ) {
		super( OnProjectileData.Hit.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnProjectileHitContext( Consumer< OnProjectileData.Hit > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onProjectileHit( ProjectileEvent.Hit event ) {
		Context.accept( CONTEXTS, new OnProjectileData.Hit( event ) );
	}
}
