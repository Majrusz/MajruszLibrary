package com.mlib.gamemodifiers.contexts;

import com.mlib.events.AnyLootModificationEvent;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnLootData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnLootContext extends Context< OnLootData > {
	static final List< OnLootContext > CONTEXTS = new ArrayList<>();

	public OnLootContext( Consumer< OnLootData > consumer, ContextParameters params ) {
		super( OnLootData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnLootContext( Consumer< OnLootData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onAnyLoot( AnyLootModificationEvent event ) {
		Context.accept( CONTEXTS, new OnLootData( event ) );
	}
}
