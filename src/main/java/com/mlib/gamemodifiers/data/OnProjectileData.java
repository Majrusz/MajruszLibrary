package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.events.ProjectileEvent;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class OnProjectileData extends ContextData.Event< ProjectileEvent > {
	public final Projectile projectile;
	public final Level level;
	@Nullable final Entity owner;
	@Nullable public final ItemStack weapon;
	@Nullable public final ItemStack arrow;
	public final CompoundTag customTag;

	protected OnProjectileData( ProjectileEvent event ) {
		super( Utility.castIfPossible( LivingEntity.class, event.projectile.getOwner() ), event );
		this.projectile = event.projectile;
		this.level = event.level;
		this.owner = event.owner;
		this.weapon = event.weapon;
		this.arrow = event.arrow;
		this.customTag = event.customTag;
	}

	public static class Shot extends OnProjectileData {
		public Shot( ProjectileEvent.Shot event ) {
			super( event );
		}
	}

	public static class Hit extends OnProjectileData {
		@Nullable public final EntityHitResult entityHitResult;
		@Nullable public final BlockHitResult blockHitResult;

		public Hit( ProjectileEvent.Hit event ) {
			super( event );
			this.entityHitResult = event.entityHitResult;
			this.blockHitResult = event.blockHitResult;
		}
	}
}