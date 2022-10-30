package com.mlib.gamemodifiers.data;

import com.mlib.events.ItemHurtEvent;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

@Deprecated
public class OnItemHurtData extends ContextData.Event< ItemHurtEvent > {
	@Nullable public final ServerPlayer player;
	public final ItemStack itemStack;

	public OnItemHurtData( ItemHurtEvent event ) {
		super( event.player, event );
		this.player = event.player;
		this.itemStack = event.itemStack;
	}
}