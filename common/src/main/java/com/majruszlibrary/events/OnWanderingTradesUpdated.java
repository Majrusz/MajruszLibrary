package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.function.Consumer;

public class OnWanderingTradesUpdated {
	public final WanderingTrader trader;
	public final MerchantOffers offers;

	public static Event< OnWanderingTradesUpdated > listen( Consumer< OnWanderingTradesUpdated > consumer ) {
		return Events.get( OnWanderingTradesUpdated.class ).add( consumer );
	}

	public OnWanderingTradesUpdated( WanderingTrader trader ) {
		this.trader = trader;
		this.offers = trader.getOffers();
	}
}
