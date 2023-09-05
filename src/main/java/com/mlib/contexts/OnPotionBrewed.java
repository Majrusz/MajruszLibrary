package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPotionBrewed {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onPotionBrewed( PlayerBrewedPotionEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements IEntityData {
		public final PlayerBrewedPotionEvent event;
		public final Player player;
		public final ItemStack itemStack;

		public Data( PlayerBrewedPotionEvent event ) {
			this.event = event;
			this.player = event.getEntity();
			this.itemStack = event.getStack();
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}