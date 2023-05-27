package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnItemFished {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onItemFished( ItemFishedEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final ItemFishedEvent event;
		public final Player player;
		public final FishingHook hook;
		public final NonNullList< ItemStack > drops;

		public Data( ItemFishedEvent event ) {
			this.event = event;
			this.player = event.getPlayer();
			this.hook = event.getHookEntity();
			this.drops = event.getDrops();
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}