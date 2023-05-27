package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemCrafted {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onItemCrafted( PlayerEvent.ItemCraftedEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final PlayerEvent.ItemCraftedEvent event;
		public final Player player;
		public final ItemStack itemStack;
		public final Container container;

		public Data( PlayerEvent.ItemCraftedEvent event ) {
			this.event = event;
			this.player = event.getPlayer();
			this.itemStack = event.getCrafting();
			this.container = event.getInventory();
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}
