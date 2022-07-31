package com.mlib.gamemodifiers.contexts;

import com.mlib.events.BlockSmeltCheckEvent;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnBlockSmeltCheckData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBlockSmeltCheckContext extends Context< OnBlockSmeltCheckData > {
	public static final Consumer< OnBlockSmeltCheckData > ENABLE_SMELT = data->data.event.shouldSmelt = true;
	static final List< OnBlockSmeltCheckContext > CONTEXTS = new ArrayList<>();

	public OnBlockSmeltCheckContext( Consumer< OnBlockSmeltCheckData > consumer, ContextParameters params ) {
		super( OnBlockSmeltCheckData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnBlockSmeltCheckContext( Consumer< OnBlockSmeltCheckData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onCheck( BlockSmeltCheckEvent event ) {
		Context.accept( CONTEXTS, new OnBlockSmeltCheckData( event ) );
	}
}
