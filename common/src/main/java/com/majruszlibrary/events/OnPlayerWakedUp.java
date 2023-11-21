package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnPlayerWakedUp implements IEntityEvent {
	public final Player player;
	public final boolean wasSleepStoppedManually; // TODO: stores invalid value on client-side

	public static Event< OnPlayerWakedUp > listen( Consumer< OnPlayerWakedUp > consumer ) {
		return Events.get( OnPlayerWakedUp.class ).add( consumer );
	}

	public OnPlayerWakedUp( Player player, boolean wasSleepStoppedManually ) {
		this.player = player;
		this.wasSleepStoppedManually = wasSleepStoppedManually;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public boolean wasSleepStoppedManually() {
		return this.wasSleepStoppedManually;
	}
}
