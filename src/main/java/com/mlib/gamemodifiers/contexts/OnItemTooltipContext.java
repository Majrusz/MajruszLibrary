package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnItemTooltipData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemTooltipContext extends Context< OnItemTooltipData > {
	static final List< OnItemTooltipContext > CONTEXTS = new ArrayList<>();

	public OnItemTooltipContext( Consumer< OnItemTooltipData > consumer, ContextParameters params ) {
		super( OnItemTooltipData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnItemTooltipContext( Consumer< OnItemTooltipData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onItemSwingDuration( ItemTooltipEvent event ) {
		Context.accept( CONTEXTS, new OnItemTooltipData( event ) );
	}
}