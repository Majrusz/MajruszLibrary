package net.mlib.contexts;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.mlib.contexts.base.Context;
import net.mlib.contexts.base.Contexts;
import net.mlib.contexts.data.IEntityData;

import java.util.function.Consumer;

public class OnBreakSpeedGet {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Player player, BlockState blockState, float speed ) {
		return Contexts.get( Data.class ).dispatch( new Data( player, blockState, speed ) );
	}

	public static class Data implements IEntityData {
		public final Player player;
		public final BlockState blockState;
		public final float originalSpeed;
		public float newSpeed;

		public Data( Player player, BlockState blockState, float speed ) {
			this.player = player;
			this.blockState = blockState;
			this.originalSpeed = speed;
			this.newSpeed = speed;
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}