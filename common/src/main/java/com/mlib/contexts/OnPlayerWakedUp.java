package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnPlayerWakedUp implements IEntityData {
	public final Player player;
	public final boolean wasSleepStoppedManually; // TODO: stores invalid value on client-side

	public static Context< OnPlayerWakedUp > listen( Consumer< OnPlayerWakedUp > consumer ) {
		return Contexts.get( OnPlayerWakedUp.class ).add( consumer );
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
