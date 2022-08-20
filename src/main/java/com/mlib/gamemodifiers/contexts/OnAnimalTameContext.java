package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnAnimalTameData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnAnimalTameContext extends Context< OnAnimalTameData > {
	static final List< OnAnimalTameContext > CONTEXTS = new ArrayList<>();

	public OnAnimalTameContext( Consumer< OnAnimalTameData > consumer, ContextParameters params ) {
		super( OnAnimalTameData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnAnimalTameContext( Consumer< OnAnimalTameData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onTame( AnimalTameEvent event ) {
		Context.accept( CONTEXTS, new OnAnimalTameData( event ) );
	}
}
