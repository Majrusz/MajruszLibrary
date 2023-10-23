package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.function.Consumer;

public class OnTradesUpdated {
	public final Player player;
	public final MerchantOffers offers;

	public static Context< OnTradesUpdated > listen( Consumer< OnTradesUpdated > consumer ) {
		return Contexts.get( OnTradesUpdated.class ).add( consumer );
	}

	public OnTradesUpdated( Player player, MerchantOffers offers ) {
		this.player = player;
		this.offers = offers;
	}
}
