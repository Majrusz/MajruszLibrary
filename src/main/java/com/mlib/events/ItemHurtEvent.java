package com.mlib.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import javax.annotation.Nullable;

public class ItemHurtEvent extends Event implements IModBusEvent {
	@Nullable public final ServerPlayer player;
	public final ItemStack itemStack;
	public final int damage;
	public int extraDamage = 0;

	public ItemHurtEvent( @Nullable ServerPlayer player, ItemStack itemStack, int damage ) {
		this.player = player;
		this.itemStack = itemStack;
		this.damage = damage;
	}

	public boolean isAboutToBroke() {
		return this.itemStack.getDamageValue() + this.extraDamage >= this.itemStack.getMaxDamage();
	}

	public boolean hasBeenBroken() {
		return this.itemStack.getDamageValue() >= this.itemStack.getMaxDamage();
	}
}
