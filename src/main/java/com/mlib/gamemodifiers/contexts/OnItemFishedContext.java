package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class OnItemFishedContext extends Context {
	static final List< OnItemFishedContext > CONTEXTS = new ArrayList<>();

	public OnItemFishedContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnItemFishedContext() {
		this( "OnItemFished", "" );
	}

	@SubscribeEvent
	public static void onDimensionChanged( ItemFishedEvent event ) {
		handleContexts( context->new Data( context, event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final ItemFishedEvent event;
		public final Player player;
		public final NonNullList< ItemStack > drops;

		Data( Context context, ItemFishedEvent event ) {
			super( context, event.getEntityLiving() );
			this.event = event;
			this.player = event.getPlayer();
			this.drops = event.getDrops();
		}
	}
}
