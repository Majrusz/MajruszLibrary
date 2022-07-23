package com.mlib.mixin;

import com.mlib.events.ProjectileEvent;
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
public abstract class MixinProjectile {
	private static final String TAG_NAME = "ProjectileExtraTags";
	CompoundTag customTag = new CompoundTag();
	@Nullable ItemStack weapon;
	@Nullable ItemStack arrow;

	public void setupArrow( @Nullable ItemStack arrow ) {
		this.arrow = arrow;
		if( Projectile.class.cast( this ).getOwner() instanceof LivingEntity entity ) {
			ItemStack itemStack = entity.getMainHandItem();
			if( itemStack.getItem() instanceof BowItem ) {
				this.weapon = itemStack;
			}
		}
	}

	public @Nullable ItemStack getWeapon() {
		return this.weapon;
	}

	public @Nullable ItemStack getArrow() {
		return this.arrow;
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "shoot(DDDFF)V", at = @At( "RETURN" ) )
	private void shoot( double x, double y, double z, float scale, float randomRange, CallbackInfo callback ) {
		MinecraftForge.EVENT_BUS.post( new ProjectileEvent.Shot( Projectile.class.cast( this ), this.weapon, this.arrow, this.customTag ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At( "RETURN" ) )
	private void onHitEntity( EntityHitResult hitResult, CallbackInfo callback ) {
		MinecraftForge.EVENT_BUS.post( new ProjectileEvent.Hit( Projectile.class.cast( this ), this.weapon, this.arrow, this.customTag, hitResult ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "onHitBlock(Lnet/minecraft/world/phys/BlockHitResult;)V", at = @At( "RETURN" ) )
	private void onHitBlock( BlockHitResult hitResult, CallbackInfo callback ) {
		MinecraftForge.EVENT_BUS.post( new ProjectileEvent.Hit( Projectile.class.cast( this ), this.weapon, this.arrow, this.customTag, hitResult ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At( "RETURN" ) )
	private void addAdditionalSaveData( CompoundTag tag, CallbackInfo callback ) {
		tag.put( TAG_NAME, this.customTag );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At( "RETURN" ) )
	private void readAdditionalSaveData( CompoundTag tag, CallbackInfo callback ) {
		this.customTag = tag.getCompound( TAG_NAME );
	}
}
