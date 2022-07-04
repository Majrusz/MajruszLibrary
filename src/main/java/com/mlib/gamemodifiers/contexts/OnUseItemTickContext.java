package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnUseItemTickData;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnUseItemTickContext extends Context< OnUseItemTickData > {
	static final List< OnUseItemTickContext > CONTEXTS = new ArrayList<>();

	public OnUseItemTickContext( Consumer< OnUseItemTickData > consumer, String configName, String configComment ) {
		super( OnUseItemTickData.class, consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnUseItemTickContext( Consumer< OnUseItemTickData > consumer ) {
		this( consumer, "OnUseItemTick", "" );
	}

	@SubscribeEvent
	public static void onTick( LivingEntityUseItemEvent.Tick event ) {
		handleContexts( new OnUseItemTickData( event ), CONTEXTS );
	}
}
