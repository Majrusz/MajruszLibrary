package com.majruszlibrary.mixin;

import com.majruszlibrary.mixininterfaces.IMixinProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( Projectile.class )
public abstract class MixinProjectile implements IMixinProjectile {
	private ItemStack majruszlibrary$weapon = ItemStack.EMPTY;
	private ItemStack majruszlibrary$arrow = ItemStack.EMPTY;

	@Override
	public ItemStack majruszlibrary$getWeapon() {
		return this.majruszlibrary$weapon;
	}

	@Override
	public ItemStack majruszlibrary$getArrow() {
		return this.majruszlibrary$arrow;
	}

	public void majruszlibrary$update( ItemStack itemStack ) {
		this.majruszlibrary$arrow = itemStack;
		if( ( ( Projectile )( Object )this ).getOwner() instanceof LivingEntity entity ) {
			this.majruszlibrary$weapon = entity.getUseItem();
		}
	}
}
