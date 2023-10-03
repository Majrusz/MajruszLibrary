package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;

import java.util.function.Consumer;

public class OnClampedRegionalDifficultyGet {
	public final Difficulty difficulty;
	public final float original;
	public float value;

	public static Context< OnClampedRegionalDifficultyGet > listen( Consumer< OnClampedRegionalDifficultyGet > consumer ) {
		return Contexts.get( OnClampedRegionalDifficultyGet.class ).add( consumer );
	}

	public OnClampedRegionalDifficultyGet( Difficulty difficulty, float original ) {
		this.difficulty = difficulty;
		this.original = original;
		this.value = original;
	}

	public float getClamped() {
		return Mth.clamp( this.value, 0.0f, 1.0f );
	}
}
