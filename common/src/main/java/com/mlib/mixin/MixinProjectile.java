package com.mlib.mixin;

import com.mlib.mixininterfaces.IMixinProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( Projectile.class )
public abstract class MixinProjectile implements IMixinProjectile {
	private ItemStack mlib$weapon = ItemStack.EMPTY;
	private ItemStack mlib$arrow = ItemStack.EMPTY;

	@Override
	public ItemStack mlib$getWeapon() {
		return this.mlib$weapon;
	}

	@Override
	public ItemStack mlib$getArrow() {
		return this.mlib$arrow;
	}

	public void mlib$update( ItemStack itemStack ) {
		this.mlib$arrow = itemStack;
		if( ( ( Projectile )( Object )this ).getOwner() instanceof LivingEntity entity ) {
			this.mlib$weapon = entity.getUseItem();
		}
	}
}
