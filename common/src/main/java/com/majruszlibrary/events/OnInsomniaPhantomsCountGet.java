package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnInsomniaPhantomsCountGet implements IEntityEvent {
	public final Player player;
	public final int original;
	public int count;

	public static Event< OnInsomniaPhantomsCountGet > listen( Consumer< OnInsomniaPhantomsCountGet > consumer ) {
		return Events.get( OnInsomniaPhantomsCountGet.class ).add( consumer );
	}

	public OnInsomniaPhantomsCountGet( Player player, int count ) {
		this.player = player;
		this.original = count;
		this.count = count;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public int getCount() {
		return Math.max( this.count, 0 );
	}
}
