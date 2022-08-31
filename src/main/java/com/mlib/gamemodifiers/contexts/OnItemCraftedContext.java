package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnItemCraftedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemCraftedContext extends ContextBase< OnItemCraftedData > {
	static final List< OnItemCraftedContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnItemCraftedContext( Consumer< OnItemCraftedData > consumer, ContextParameters params ) {
		super( OnItemCraftedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnItemCraftedContext( Consumer< OnItemCraftedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onItemCrafted( PlayerEvent.ItemCraftedEvent event ) {
		ContextBase.accept( CONTEXTS, new OnItemCraftedData( event ) );
	}
}
