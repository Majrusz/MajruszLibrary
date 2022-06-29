package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnPlayerInteractData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerInteractContext extends Context< OnPlayerInteractData > {
	static final List< OnPlayerInteractContext > CONTEXTS = new ArrayList<>();

	public OnPlayerInteractContext( Consumer< OnPlayerInteractData > consumer, String configName, String configComment ) {
		super( consumer, configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPlayerInteractContext( Consumer< OnPlayerInteractData > consumer ) {
		this( consumer, "OnPlayerInteract", "" );
	}

	@SubscribeEvent public static void onEntityInteract( PlayerInteractEvent.EntityInteract event ) {
		handleContexts( new OnPlayerInteractData( event, Utility.castIfPossible( LivingEntity.class, event.getTarget() ) ), CONTEXTS );
	}

	@SubscribeEvent public static void onRightClickBlock( PlayerInteractEvent.RightClickBlock event ) {
		handleContexts( new OnPlayerInteractData( event, event.getEntityLiving() ), CONTEXTS );
	}

	@SubscribeEvent public static void onRightClickItem( PlayerInteractEvent.RightClickItem event ) {
		handleContexts( new OnPlayerInteractData( event, event.getEntityLiving() ), CONTEXTS );
	}
}
