package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnPlayerInteractContext extends Context {
	static final List< OnPlayerInteractContext > CONTEXTS = new ArrayList<>();

	public OnPlayerInteractContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnPlayerInteractContext() {
		this( "OnPlayerInteract", "" );
	}

	@SubscribeEvent
	public static void onEntityInteract( PlayerInteractEvent.EntityInteract event ) {
		handleContexts( new Data( event, Utility.castIfPossible( LivingEntity.class, event.getTarget() ) ), CONTEXTS );
	}

	@SubscribeEvent
	public static void onRightClickBlock( PlayerInteractEvent.RightClickBlock event ) {
		handleContexts( new Data( event, event.getEntityLiving() ), CONTEXTS );
	}

	@SubscribeEvent
	public static void onRightClickItem( PlayerInteractEvent.RightClickItem event ) {
		handleContexts( new Data( event, event.getEntityLiving() ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final PlayerInteractEvent event;
		public final ItemStack itemStack;
		public final Player player;
		@Nullable
		public final LivingEntity target;

		public Data( PlayerInteractEvent event, @Nullable LivingEntity target ) {
			super( event.getEntityLiving() );
			this.event = event;
			this.itemStack = event.getItemStack();
			this.player = event.getPlayer();
			this.target = target;
		}
	}
}
