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
	@Nullable public final Entity owner;
	@Nullable public final ItemStack weapon;
	@Nullable public final ItemStack arrow;
	public final CompoundTag customTag;

	public ProjectileEvent( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag ) {
		this.projectile = projectile;
		this.level = projectile.level;
		this.owner = projectile.getOwner();
		this.weapon = weapon;
		this.arrow = arrow;
		this.customTag = customTag;
	}

	public static class Shot extends ProjectileEvent {
		public Shot( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag ) {
			super( projectile, weapon, arrow, customTag );
		}
	}

	public static class Hit extends ProjectileEvent {
		@Nullable public final EntityHitResult entityHitResult;
		@Nullable public final BlockHitResult blockHitResult;

		public Hit( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag,
			@Nullable EntityHitResult entityHitResult
		) {
			super( projectile, weapon, arrow, customTag );
			this.entityHitResult = entityHitResult;
			this.blockHitResult = null;
		}

		public Hit( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag,
			@Nullable BlockHitResult blockHitResult
		) {
			super( projectile, weapon, arrow, customTag );
			this.entityHitResult = null;
			this.blockHitResult = blockHitResult;
		}
	}
}
