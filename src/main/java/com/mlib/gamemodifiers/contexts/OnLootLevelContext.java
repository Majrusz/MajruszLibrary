package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnBreakSpeedData;
import com.mlib.gamemodifiers.data.OnLootLevelData;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnLootLevelContext extends Context< OnLootLevelData > {
	static final List< OnLootLevelContext > CONTEXTS = new ArrayList<>();

	public OnLootLevelContext( Consumer< OnLootLevelData > consumer, String configName, String configComment ) {
		super( OnLootLevelData.class, consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnLootLevelContext( Consumer< OnLootLevelData > consumer ) {
		this( consumer, "OnLootLevel", "" );
	}

	@SubscribeEvent
	public static void onLootLevel( LootingLevelEvent event ) {
		handleContexts( new OnLootLevelData( event ), CONTEXTS );
	}
}
