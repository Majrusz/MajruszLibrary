package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Consumer;

public class OnMobSpawnLimitGet {
	public final MobCategory category;
	public final int original;
	public int value;

	public static Context< OnMobSpawnLimitGet > listen( Consumer< OnMobSpawnLimitGet > consumer ) {
		return Contexts.get( OnMobSpawnLimitGet.class ).add( consumer );
	}

	public OnMobSpawnLimitGet( MobCategory category, int limit ) {
		this.category = category;
		this.original = limit;
		this.value = limit;
	}

	public int getSpawnLimit() {
		return Math.max( this.value, 1 );
	}
}
