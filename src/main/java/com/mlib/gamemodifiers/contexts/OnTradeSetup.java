package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.function.Consumer;

public class OnTradeSetup {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
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
