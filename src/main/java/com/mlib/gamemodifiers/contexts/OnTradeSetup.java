package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnTradeSetup {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final List< Context > CONTEXTS = new ArrayList<>();

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( Data.class, consumer, params );
			Context.addSorted( CONTEXTS, this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onTradeSetup( VillagerTradesEvent event ) {
			Context.accept( CONTEXTS, new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< VillagerTradesEvent > {
		public final VillagerProfession profession;

		public Data( VillagerTradesEvent event ) {
			super( ( LivingEntity )null, event );
			this.profession = event.getType();
		}

		public List< VillagerTrades.ItemListing > getTrades( int tier ) {
			return this.event.getTrades().get( tier );
		}
	}
}
