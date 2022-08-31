package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnLootTableLoadData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnLootTableLoadContext extends ContextBase< OnLootTableLoadData > {
	static final List< OnLootTableLoadContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnLootTableLoadContext( Consumer< OnLootTableLoadData > consumer, ContextParameters params ) {
		super( OnLootTableLoadData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnLootTableLoadContext( Consumer< OnLootTableLoadData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onLootTableLoad( LootTableLoadEvent event ) {
		ContextBase.accept( CONTEXTS, new OnLootTableLoadData( event ) );
	}
}
