package com.mlib.mixin;

import com.mlib.events.ProjectileShotEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin( Projectile.class )
public abstract class MixinProjectile {
	static final String TAG_NAME = "ProjectileExtraTags";
	CompoundTag customTag = new CompoundTag();
	@Nullable protected ItemStack itemStack;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "shoot(DDDFF)V", at = @At( "RETURN" ) )
	private void shoot( double x, double y, double z, float scale, float randomRange, CallbackInfo callback ) {
		Projectile projectile = ( Projectile )( Object )this;
		MinecraftForge.EVENT_BUS.post( new ProjectileShotEvent( projectile, this.itemStack, this.customTag ) );
	}

	private void addAdditionalSaveData( CompoundTag tag ) {
		tag.put( TAG_NAME, this.customTag );
	}

	private void readAdditionalSaveData( CompoundTag tag ) {
		this.customTag = tag.getCompound( TAG_NAME );
	}
}
