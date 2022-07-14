package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ItemSwingDurationEvent;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnItemSwingDurationData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemSwingDurationContext extends Context< OnItemSwingDurationData > {
	static final List< OnItemSwingDurationContext > CONTEXTS = new ArrayList<>();

	public OnItemSwingDurationContext( Consumer< OnItemSwingDurationData > consumer, ContextParameters params ) {
		super( OnItemSwingDurationData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnItemSwingDurationContext( Consumer< OnItemSwingDurationData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onItemSwingDuration( ItemSwingDurationEvent event ) {
		Context.accept( CONTEXTS, new OnItemSwingDurationData( event ) );
	}
}
