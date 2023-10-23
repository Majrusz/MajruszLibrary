package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnItemTraded {
	public final Villager villager;
	public final @Nullable Player player;
	public final MerchantOffer offer;

	public static Context< OnItemTraded > listen( Consumer< OnItemTraded > consumer ) {
		return Contexts.get( OnItemTraded.class ).add( consumer );
	}

	public OnItemTraded( Villager villager, MerchantOffer offer ) {
		this.villager = villager;
		this.player = villager.getTradingPlayer();
		this.offer = offer;
	}
}
