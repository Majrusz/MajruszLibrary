package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ProjectileEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnProjectileData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Deprecated
@Mod.EventBusSubscriber
public class OnProjectileShotContext extends ContextBase< OnProjectileData.Shot > {
	static final List< OnProjectileShotContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnProjectileShotContext( Consumer< OnProjectileData.Shot > consumer, ContextParameters params ) {
		super( OnProjectileData.Shot.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnProjectileShotContext( Consumer< OnProjectileData.Shot > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onProjectileShot( ProjectileEvent.Shot event ) {
		ContextBase.accept( CONTEXTS, new OnProjectileData.Shot( event ) );
	}
}
