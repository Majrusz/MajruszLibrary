package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.function.Consumer;

public class OnTradesUpdated implements IEntityData {
	public final Villager villager;
	public final MerchantOffers offers;
	public final Player player;

	public static Context< OnTradesUpdated > listen( Consumer< OnTradesUpdated > consumer ) {
		return Contexts.get( OnTradesUpdated.class ).add( consumer );
	}

	public OnTradesUpdated( Villager villager, Player player ) {
		this.villager = villager;
		this.offers = villager.getOffers();
		this.player = player;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
