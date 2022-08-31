package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnPlayerInteractData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerInteractContext extends ContextBase< OnPlayerInteractData > {
	static final List< OnPlayerInteractContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnPlayerInteractContext( Consumer< OnPlayerInteractData > consumer, ContextParameters params ) {
		super( OnPlayerInteractData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnPlayerInteractContext( Consumer< OnPlayerInteractData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onEntityInteract( PlayerInteractEvent.EntityInteract event ) {
		ContextBase.accept( CONTEXTS, new OnPlayerInteractData( event, Utility.castIfPossible( LivingEntity.class, event.getTarget() ) ) );
	}

	@SubscribeEvent
	public static void onRightClickBlock( PlayerInteractEvent.RightClickBlock event ) {
		ContextBase.accept( CONTEXTS, new OnPlayerInteractData( event, event.getEntityLiving() ) );
	}

	@SubscribeEvent
	public static void onRightClickItem( PlayerInteractEvent.RightClickItem event ) {
		ContextBase.accept( CONTEXTS, new OnPlayerInteractData( event, event.getEntityLiving() ) );
	}
}
