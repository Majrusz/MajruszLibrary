package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnTradeSetup {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onTradeSetup( VillagerTradesEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data {
		public final VillagerTradesEvent event;
		public final VillagerProfession profession;

		public Data( VillagerTradesEvent event ) {
			this.event = event;
			this.profession = event.getType();
		}

		public List< VillagerTrades.ItemListing > getTrades( int tier ) {
			return this.event.getTrades().get( tier );
		}
	}
}
