package com.mlib.mixininterfaces;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IMixinProjectile {
	@Nullable
	ItemStack getWeapon();

	@Nullable
	ItemStack getArrow();
}
