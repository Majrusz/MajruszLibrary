package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Consumer;

public class OnMobSpawnLimitGet {
	public final MobCategory category;
	public final int original;
	public int value;

	public static Event< OnMobSpawnLimitGet > listen( Consumer< OnMobSpawnLimitGet > consumer ) {
		return Events.get( OnMobSpawnLimitGet.class ).add( consumer );
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
