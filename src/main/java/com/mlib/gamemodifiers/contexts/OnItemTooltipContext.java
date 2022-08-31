package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnItemTooltipData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Deprecated
@Mod.EventBusSubscriber
public class OnItemTooltipContext extends ContextBase< OnItemTooltipData > {
	static final List< OnItemTooltipContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnItemTooltipContext( Consumer< OnItemTooltipData > consumer, ContextParameters params ) {
		super( OnItemTooltipData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnItemTooltipContext( Consumer< OnItemTooltipData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onItemSwingDuration( ItemTooltipEvent event ) {
		ContextBase.accept( CONTEXTS, new OnItemTooltipData( event ) );
	}
}
