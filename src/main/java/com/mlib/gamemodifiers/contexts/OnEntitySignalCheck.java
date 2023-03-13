package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnEntitySignalCheck {
	public static final Consumer< Data > DISPATCH = data->data.dispatch = true;

	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ServerLevel level, BlockPos position, Player player ) {
		Data data = new Data( level, position, player );
		boolean isNotTriggeredByPlayer = !position.equals( player.blockPosition() );

		return isNotTriggeredByPlayer ? Contexts.get( Data.class ).dispatch( data ) : data;
	}

	public static class Data implements ILevelData {
		public final ServerLevel level;
		public final BlockPos position;
		public final Player player;
		boolean dispatch = false;

		public Data( ServerLevel level, BlockPos position, Player player ) {
			this.level = level;
			this.position = position;
			this.player = player;
		}

		public boolean shouldListen() {
			return this.dispatch;
		}

		@Override
		public Level getLevel() {
			return this.level;
		}
	}
}
