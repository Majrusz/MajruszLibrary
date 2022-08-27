package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnPotionBrewedData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPotionBrewedContext extends ContextBase< OnPotionBrewedData > {
	static final List< OnPotionBrewedContext > CONTEXTS = new ArrayList<>();

	public OnPotionBrewedContext( Consumer< OnPotionBrewedData > consumer, ContextParameters params ) {
		super( OnPotionBrewedData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnPotionBrewedContext( Consumer< OnPotionBrewedData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onPotionBrewed( PlayerBrewedPotionEvent event ) {
		ContextBase.accept( CONTEXTS, new OnPotionBrewedData( event ) );
	}
}
