package com.mlib.mixininterfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface IMixinProjectile {
	static ItemStack mlib$getProjectileWeapon( Entity entity ) {
		return entity instanceof IMixinProjectile projectile ? projectile.mlib$getWeapon() : ItemStack.EMPTY;
	}

	static ItemStack mlib$getProjectileArrow( Entity entity ) {
		return entity instanceof IMixinProjectile projectile ? projectile.mlib$getArrow() : ItemStack.EMPTY;
	}

	ItemStack mlib$getWeapon();

	ItemStack mlib$getArrow();
}
