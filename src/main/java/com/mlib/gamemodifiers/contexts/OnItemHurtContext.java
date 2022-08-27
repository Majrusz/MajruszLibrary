package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ItemHurtEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnItemHurtData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemHurtContext extends ContextBase< OnItemHurtData > {
	static final List< OnItemHurtContext > CONTEXTS = new ArrayList<>();

	public OnItemHurtContext( Consumer< OnItemHurtData > consumer, ContextParameters params ) {
		super( OnItemHurtData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnItemHurtContext( Consumer< OnItemHurtData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onItemHurt( ItemHurtEvent event ) {
		ContextBase.accept( CONTEXTS, new OnItemHurtData( event ) );
	}
}
