package com.mlib.mixin;

import com.mlib.events.ProjectileEvent;
import com.mlib.mixininterfaces.IMixinProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin( Projectile.class )
public abstract class MixinProjectile implements IMixinProjectile {
	private static final String TAG_NAME = "ProjectileExtraTags";
	CompoundTag mlibCustomTag = new CompoundTag();
	@Nullable ItemStack mlibWeapon;
	@Nullable ItemStack mlibArrow;

	public void setupArrow( @Nullable ItemStack arrow ) {
		this.mlibArrow = arrow;
		if( Projectile.class.cast( this ).getOwner() instanceof LivingEntity entity ) {
			ItemStack itemStack = entity.getMainHandItem();
			if( itemStack.getItem() instanceof BowItem ) {
				this.mlibWeapon = itemStack;
			}
		}
	}

	@Override
	public @Nullable ItemStack getWeapon() {
		return this.mlibWeapon;
	}

	@Override
	public @Nullable ItemStack getArrow() {
		return this.mlibArrow;
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "shoot(DDDFF)V", at = @At( "RETURN" ) )
	private void shoot( double x, double y, double z, float scale, float randomRange, CallbackInfo callback ) {
		MinecraftForge.EVENT_BUS.post( new ProjectileEvent.Shot( Projectile.class.cast( this ), this.mlibWeapon, this.mlibArrow, this.mlibCustomTag ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At( "RETURN" ) )
	private void onHitEntity( EntityHitResult hitResult, CallbackInfo callback ) {
		MinecraftForge.EVENT_BUS.post( new ProjectileEvent.Hit( Projectile.class.cast( this ), this.mlibWeapon, this.mlibArrow, this.mlibCustomTag, hitResult ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "onHitBlock(Lnet/minecraft/world/phys/BlockHitResult;)V", at = @At( "RETURN" ) )
	private void onHitBlock( BlockHitResult hitResult, CallbackInfo callback ) {
		MinecraftForge.EVENT_BUS.post( new ProjectileEvent.Hit( Projectile.class.cast( this ), this.mlibWeapon, this.mlibArrow, this.mlibCustomTag, hitResult ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At( "RETURN" ) )
	private void addAdditionalSaveData( CompoundTag tag, CallbackInfo callback ) {
		tag.put( TAG_NAME, this.mlibCustomTag );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At( "RETURN" ) )
	private void readAdditionalSaveData( CompoundTag tag, CallbackInfo callback ) {
		this.mlibCustomTag = tag.getCompound( TAG_NAME );
	}
}
