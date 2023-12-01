package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnItemTraded implements IEntityEvent {
	public final Villager villager;
	public final @Nullable Player player;
	public final MerchantOffer offer;

	public static Event< OnItemTraded > listen( Consumer< OnItemTraded > consumer ) {
		return Events.get( OnItemTraded.class ).add( consumer );
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
