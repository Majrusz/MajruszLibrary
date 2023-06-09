package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import com.mlib.gamemodifiers.data.IPositionData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnProjectileHit {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag, HitResult hitResult ) {
		return Contexts.get( Data.class ).dispatch( new Data( projectile, weapon, arrow, customTag, hitResult ) );
	}

	public static class Data implements ILevelData, IPositionData {
		public final Projectile projectile;
		public final Level level;
		@Nullable public final Entity owner;
		@Nullable public final ItemStack weapon;
		@Nullable public final ItemStack arrow;
		public final CompoundTag customTag;
		public final HitResult hitResult;

		public Data( Projectile projectile, @Nullable ItemStack weapon, @Nullable ItemStack arrow, CompoundTag customTag, HitResult hitResult ) {
			this.projectile = projectile;
			this.level = projectile.level();
			this.owner = projectile.getOwner();
			this.weapon = weapon;
			this.arrow = arrow;
			this.customTag = customTag;
			this.hitResult = hitResult;
		}

		@Override
		public Level getLevel() {
			return this.level;
		}

		@Override
		public Vec3 getPosition() {
			return this.projectile.getPosition( 0.0f );
		}

		@Nullable
		public EntityHitResult getEntityHitResult() {
			return Utility.castIfPossible( EntityHitResult.class, this.hitResult );
		}

		@Nullable
		public BlockHitResult getBlockHitResult() {
			return Utility.castIfPossible( BlockHitResult.class, this.hitResult );
		}
	}
}
