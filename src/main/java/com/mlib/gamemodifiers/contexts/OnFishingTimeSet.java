package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnFishingTimeSet {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( FishingHook hook, int timeUntilLured ) {
		return Contexts.get( Data.class ).dispatch( new Data( hook, timeUntilLured ) );
	}

	public static Condition< Data > hasPlayer() {
		return new Condition<>( data->data.player != null );
	}

	public static class Data implements IEntityData {
		public FishingHook hook;
		public final int original;
		public int ticks;
		@Nullable
		public Player player;

		public Data( FishingHook hook, int original ) {
			this.hook = hook;
			this.original = original;
			this.ticks = original;
			this.player = hook.getPlayerOwner();
		}

		public int getTicks() {
			return Math.max( this.ticks, 1 );
		}

		@Override
		public Entity getEntity() {
			return this.hook;
		}
	}
}
