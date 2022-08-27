package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ItemSwingDurationEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnItemSwingDurationData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemSwingDurationContext extends ContextBase< OnItemSwingDurationData > {
	static final List< OnItemSwingDurationContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnItemSwingDurationContext( Consumer< OnItemSwingDurationData > consumer, ContextParameters params ) {
		super( OnItemSwingDurationData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnItemSwingDurationContext( Consumer< OnItemSwingDurationData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onItemSwingDuration( ItemSwingDurationEvent event ) {
		ContextBase.accept( CONTEXTS, new OnItemSwingDurationData( event ) );
	}
}
