package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;

import java.util.function.Consumer;

public class OnClampedRegionalDifficultyGet {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Difficulty difficulty, float original ) {
		return Contexts.get( Data.class ).dispatch( new Data( difficulty, original ) );
	}

	public static class Data {
		public final Difficulty difficulty;
		public final float original;
		public float value;

		public Data( Difficulty difficulty, float original ) {
			this.difficulty = difficulty;
			this.original = original;
			this.value = original;
		}

		public float getClamped() {
			return Mth.clamp( this.value, 0.0f, 1.0f );
		}
	}
}
