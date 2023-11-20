package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnItemTraded implements IEntityData {
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

	@Override
	public Entity getEntity() {
		return this.villager;
	}
}
