package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntitySignalReceived {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ServerLevel level, BlockPos position, Player player, @Nullable Entity owner, @Nullable Entity ownersProjectile,
		float distance
	) {
		return Contexts.get( Data.class ).dispatch( new Data( level, position, player, owner, ownersProjectile, distance ) );
	}

	public static class Data implements ILevelData {
		public final ServerLevel level;
		public final BlockPos position;
		public final Player player;
		public final @Nullable Entity owner;
		public final @Nullable Entity ownersProjectile;
		public final float distance;

		public Data( ServerLevel level, BlockPos position, Player player, @Nullable Entity owner, @Nullable Entity ownersProjectile, float distance ) {
			this.level = level;
			this.position = position;
			this.player = player;
			this.owner = owner;
			this.ownersProjectile = ownersProjectile;
			this.distance = distance;
		}

		@Override
		public Level getLevel() {
			return this.level;
		}
	}
}
