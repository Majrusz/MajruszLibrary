package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnBabySpawnData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBabySpawnContext extends ContextBase< OnBabySpawnData > {
	static final List< OnBabySpawnContext > CONTEXTS = new ArrayList<>();

	public OnBabySpawnContext( Consumer< OnBabySpawnData > consumer, ContextParameters params ) {
		super( OnBabySpawnData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnBabySpawnContext( Consumer< OnBabySpawnData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onBreed( BabyEntitySpawnEvent event ) {
		ContextBase.accept( CONTEXTS, new OnBabySpawnData( event ) );
	}
}
