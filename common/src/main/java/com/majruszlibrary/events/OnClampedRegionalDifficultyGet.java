package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;

import java.util.function.Consumer;

public class OnClampedRegionalDifficultyGet {
	public final Difficulty difficulty;
	public final float original;
	public float crd;

	public static Event< OnClampedRegionalDifficultyGet > listen( Consumer< OnClampedRegionalDifficultyGet > consumer ) {
		return Events.get( OnClampedRegionalDifficultyGet.class ).add( consumer );
	}

	public OnClampedRegionalDifficultyGet( Difficulty difficulty, float crd ) {
		this.difficulty = difficulty;
		this.original = crd;
		this.crd = crd;
	}

	public float getClamped() {
		return Mth.clamp( this.crd, 0.0f, 1.0f );
	}
}
