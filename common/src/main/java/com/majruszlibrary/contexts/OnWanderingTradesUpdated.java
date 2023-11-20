package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.function.Consumer;

public class OnWanderingTradesUpdated {
	public final WanderingTrader trader;
	public final MerchantOffers offers;

	public static Context< OnWanderingTradesUpdated > listen( Consumer< OnWanderingTradesUpdated > consumer ) {
		return Contexts.get( OnWanderingTradesUpdated.class ).add( consumer );
	}

	public OnWanderingTradesUpdated( WanderingTrader trader ) {
		this.trader = trader;
		this.offers = trader.getOffers();
	}
}
