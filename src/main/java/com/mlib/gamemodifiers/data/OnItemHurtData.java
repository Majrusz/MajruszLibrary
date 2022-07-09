package com.mlib.gamemodifiers.data;

import com.mlib.events.ItemHurtEvent;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class OnItemHurtData extends ContextData {
	public final ItemHurtEvent event;
	@Nullable public final ServerPlayer player;
	public final ItemStack itemStack;

	public OnItemHurtData( ItemHurtEvent event ) {
		super( event.player );
		this.event = event;
		this.player = event.player;
		this.itemStack = event.itemStack;
	}
}