package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ICancellableData;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnEnderManAngered implements ICancellableData, IEntityData {
	public final EnderMan enderMan;
	public final Player player;
	private boolean isAngerCancelled = false;

	public static Context< OnEnderManAngered > listen( Consumer< OnEnderManAngered > consumer ) {
		return Contexts.get( OnEnderManAngered.class ).add( consumer );
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
