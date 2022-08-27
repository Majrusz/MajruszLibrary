package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnItemFishedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemFishedContext extends ContextBase< OnItemFishedData > {
	static final List< OnItemFishedContext > CONTEXTS = new ArrayList<>();

	public OnItemFishedContext( Consumer< OnItemFishedData > consumer, ContextParameters params ) {
		super( OnItemFishedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnItemFishedContext( Consumer< OnItemFishedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onDimensionChanged( ItemFishedEvent event ) {
		ContextBase.accept( CONTEXTS, new OnItemFishedData( event ) );
	}
}
