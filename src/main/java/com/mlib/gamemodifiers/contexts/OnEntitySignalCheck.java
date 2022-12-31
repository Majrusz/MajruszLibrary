package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnEntitySignalCheck {
	public static final Consumer< Data > DISPATCH = data->data.dispatch = true;

	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public static Data accept( Data data ) {
			boolean isNotTriggeredByPlayer = !data.position.equals( data.player.blockPosition() );

			return isNotTriggeredByPlayer ? CONTEXTS.accept( data ) : data;
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
		boolean dispatch = false;

		public Data( ServerLevel level, BlockPos position, Player player ) {
			super( player );

			this.level = level;
			this.position = position;
			this.player = player;
		}

		public boolean shouldListen() {
			return this.dispatch;
		}
	}
}
