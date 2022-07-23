package com.mlib.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import javax.annotation.Nullable;

public class ProjectileEvent extends Event implements IModBusEvent {
	public final Projectile projectile;
	public final Level level;
	@Nullable final Entity owner;
	@Nullable public final ItemStack itemStack;
	public final CompoundTag customTag;

	public ProjectileEvent( Projectile projectile, @Nullable ItemStack itemStack, CompoundTag customTag ) {
		this.projectile = projectile;
		this.level = projectile.level;
		this.owner = projectile.getOwner();
		this.itemStack = itemStack;
		this.customTag = customTag;
	}

	public static class Shot extends ProjectileEvent {
		public Shot( Projectile projectile, @Nullable ItemStack itemStack, CompoundTag customTag ) {
			super( projectile, itemStack, customTag );
		}
	}

	public static class Hit extends ProjectileEvent {
		@Nullable public final EntityHitResult entityHitResult;
		@Nullable public final BlockHitResult blockHitResult;

		public Hit( Projectile projectile, @Nullable ItemStack itemStack, CompoundTag customTag, @Nullable EntityHitResult entityHitResult ) {
			super( projectile, itemStack, customTag );
			this.entityHitResult = entityHitResult;
			this.blockHitResult = null;
		}

		public Hit( Projectile projectile, @Nullable ItemStack itemStack, CompoundTag customTag, @Nullable BlockHitResult blockHitResult ) {
			super( projectile, itemStack, customTag );
			this.entityHitResult = null;
			this.blockHitResult = blockHitResult;
		}
	}
}
