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
public class OnProjectileShotContext extends Context< OnProjectileData.Shot > {
	static final List< OnProjectileShotContext > CONTEXTS = new ArrayList<>();

	public OnProjectileShotContext( Consumer< OnProjectileData.Shot > consumer, ContextParameters params ) {
		super( OnProjectileData.Shot.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnProjectileShotContext( Consumer< OnProjectileData.Shot > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onProjectileShot( ProjectileEvent.Shot event ) {
		Context.accept( CONTEXTS, new OnProjectileData.Shot( event ) );
	}
}
