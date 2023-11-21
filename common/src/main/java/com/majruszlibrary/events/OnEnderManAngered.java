package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ICancellableEvent;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnEnderManAngered implements ICancellableEvent, IEntityEvent {
	public final EnderMan enderMan;
	public final Player player;
	private boolean isAngerCancelled = false;

	public static Event< OnEnderManAngered > listen( Consumer< OnEnderManAngered > consumer ) {
		return Events.get( OnEnderManAngered.class ).add( consumer );
	}

	public OnEnderManAngered( EnderMan enderMan, Player player ) {
		this.enderMan = enderMan;
		this.player = player;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isAngerCancelled();
	}

	@Override
	public Entity getEntity() {
		return this.enderMan;
	}

	public void cancelAnger() {
		this.isAngerCancelled = true;
	}

	public boolean isAngerCancelled() {
		return this.isAngerCancelled;
	}
}
