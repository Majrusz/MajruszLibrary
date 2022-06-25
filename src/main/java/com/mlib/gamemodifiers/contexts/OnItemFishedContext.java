package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
		handleContexts( new Data( event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final ItemFishedEvent event;
		public final LivingEntity entity;
		public final NonNullList< ItemStack > drops;

		public Data( ItemFishedEvent event ) {
			super( event.getEntityLiving() );
			this.event = event;
			this.entity = event.getEntityLiving();
			this.drops = event.getDrops();
		}
	}
}
