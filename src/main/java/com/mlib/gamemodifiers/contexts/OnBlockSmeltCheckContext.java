package com.mlib.gamemodifiers.contexts;

import com.mlib.events.BlockSmeltCheckEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnBlockSmeltCheckData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Deprecated
@Mod.EventBusSubscriber
public class OnBlockSmeltCheckContext extends ContextBase< OnBlockSmeltCheckData > {
	public static final Consumer< OnBlockSmeltCheckData > ENABLE_SMELT = data->data.event.shouldSmelt = true;
	static final List< OnBlockSmeltCheckContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnBlockSmeltCheckContext( Consumer< OnBlockSmeltCheckData > consumer, ContextParameters params ) {
		super( OnBlockSmeltCheckData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnBlockSmeltCheckContext( Consumer< OnBlockSmeltCheckData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onCheck( BlockSmeltCheckEvent event ) {
		ContextBase.accept( CONTEXTS, new OnBlockSmeltCheckData( event ) );
	}
}
