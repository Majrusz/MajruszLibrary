package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnDeathData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDeathContext extends ContextBase< OnDeathData > {
	static final List< OnDeathContext > CONTEXTS = new ArrayList<>();

	public OnDeathContext( Consumer< OnDeathData > consumer, ContextParameters params ) {
		super( OnDeathData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnDeathContext( Consumer< OnDeathData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onDamaged( LivingDeathEvent event ) {
		ContextBase.accept( CONTEXTS, new OnDeathData( event ) );
	}
}
