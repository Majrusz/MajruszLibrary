package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnAnimalTameData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnAnimalTameContext extends ContextBase< OnAnimalTameData > {
	static final List< OnAnimalTameContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnAnimalTameContext( Consumer< OnAnimalTameData > consumer, ContextParameters params ) {
		super( OnAnimalTameData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnAnimalTameContext( Consumer< OnAnimalTameData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onTame( AnimalTameEvent event ) {
		ContextBase.accept( CONTEXTS, new OnAnimalTameData( event ) );
	}
}
