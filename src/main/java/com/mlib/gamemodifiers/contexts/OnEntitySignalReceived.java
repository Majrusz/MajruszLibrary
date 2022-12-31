package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntitySignalReceived {
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public static Data accept( Data data ) {
			return CONTEXTS.accept( data );
		}

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}
	}

	public static class Data extends ContextData {
		public final ServerLevel level;
		public final BlockPos position;
		public final Player player;
		public final @Nullable Entity owner;
		public final @Nullable Entity ownersProjectile;
		public final float distance;

		public Data( ServerLevel level, BlockPos position, Player player, @Nullable Entity owner, @Nullable Entity ownersProjectile, float distance ) {
			super( player );

			this.level = level;
			this.position = position;
			this.player = player;
			this.owner = owner;
			this.ownersProjectile = ownersProjectile;
			this.distance = distance;
		}
	}
}
