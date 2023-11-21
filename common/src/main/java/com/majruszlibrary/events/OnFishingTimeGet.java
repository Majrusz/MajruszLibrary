package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnFishingTimeGet implements IEntityEvent {
	public final FishingHook hook;
	public final @Nullable Player player;
	public final int original;
	public int time;

	public static Event< OnFishingTimeGet > listen( Consumer< OnFishingTimeGet > consumer ) {
		return Events.get( OnFishingTimeGet.class ).add( consumer );
	}

	public OnFishingTimeGet( FishingHook hook, int time ) {
		this.hook = hook;
		this.player = hook.getPlayerOwner();
		this.original = time;
		this.time = time;
	}

	@Override
	public Entity getEntity() {
		return this.hook;
	}

	public int getTicks() {
		return Math.max( this.time, 1 );
	}
}
