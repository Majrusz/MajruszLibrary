package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnUseItemTickData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnUseItemTickContext extends Context< OnUseItemTickData > {
	static final List< OnUseItemTickContext > CONTEXTS = new ArrayList<>();

	public OnUseItemTickContext( Consumer< OnUseItemTickData > consumer, ContextParameters params ) {
		super( OnUseItemTickData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnUseItemTickContext( Consumer< OnUseItemTickData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onTick( LivingEntityUseItemEvent.Tick event ) {
		Context.accept( CONTEXTS, new OnUseItemTickData( event ) );
	}
}
