package com.mlib.mixininterfaces;

import com.mlib.Utility;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IMixinProjectile {
	static ItemStack getWeaponFromDirectEntity( DamageSource source ) {
		IMixinProjectile projectile = Utility.castIfPossible( IMixinProjectile.class, source.getDirectEntity() );
		return projectile != null ? projectile.getWeapon() : ItemStack.EMPTY;
	}

	static ItemStack getArrowFromDirectEntity( DamageSource source ) {
		IMixinProjectile projectile = Utility.castIfPossible( IMixinProjectile.class, source.getDirectEntity() );
		return projectile != null ? projectile.getArrow() : ItemStack.EMPTY;
	}

	@Nullable
	ItemStack getWeapon();

	@Nullable
	ItemStack getArrow();
}
