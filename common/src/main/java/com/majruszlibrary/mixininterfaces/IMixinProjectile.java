package com.majruszlibrary.mixininterfaces;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface IMixinProjectile {
	static ItemStack majruszlibrary$getProjectileWeapon( Entity entity ) {
		return entity instanceof IMixinProjectile projectile ? projectile.majruszlibrary$getWeapon() : ItemStack.EMPTY;
	}

	static ItemStack majruszlibrary$getProjectileArrow( Entity entity ) {
		return entity instanceof IMixinProjectile projectile ? projectile.majruszlibrary$getArrow() : ItemStack.EMPTY;
	}

	ItemStack majruszlibrary$getWeapon();

	ItemStack majruszlibrary$getArrow();
}
