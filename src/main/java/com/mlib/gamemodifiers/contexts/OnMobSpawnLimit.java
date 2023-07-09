package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.Priority;
import net.minecraft.world.entity.MobCategory;

import java.util.Arrays;
import java.util.function.Consumer;

public class OnMobSpawnLimit {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( MobCategory category, int limit ) {
		return Contexts.get( Data.class ).dispatch( new Data( category, limit ) );
	}

	public static Condition< Data > is( MobCategory... categories ) {
		return new Condition< Data >( data->Arrays.asList( categories ).contains( data.category ) )
			.priority( Priority.HIGH );
	}

	public static class Data {
		public final MobCategory category;
		public final int original;
		public int value;

		public Data( MobCategory category, int limit ) {
			this.category = category;
			this.original = limit;
			this.value = limit;
		}

		public int getSpawnLimit() {
			return Math.max( this.value, 1 );
		}
	}
}
