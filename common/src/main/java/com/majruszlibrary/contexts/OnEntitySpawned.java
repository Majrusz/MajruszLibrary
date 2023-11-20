package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.ICancellableData;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnEntitySpawned implements ICancellableData, IEntityData {
	public final Entity entity;
	public final boolean isLoadedFromDisk;
	public final BlockPos position;
	public final DifficultyInstance difficulty;
	private boolean isSpawnCancelled = false;

	public static Context< OnEntitySpawned > listen( Consumer< OnEntitySpawned > consumer ) {
		return Contexts.get( OnEntitySpawned.class ).add( consumer );
	}

	public OnEntitySpawned( Entity entity, boolean isLoadedFromDisk ) {
		this.entity = entity;
		this.isLoadedFromDisk = isLoadedFromDisk;
		this.position = entity.blockPosition();
		this.difficulty = entity.level().getCurrentDifficultyAt( this.position );
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isSpawnCancelled();
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public void cancelSpawn() {
		this.isSpawnCancelled = true;
	}

	public boolean isSpawnCancelled() {
		return this.isSpawnCancelled
			&& !( this.entity instanceof Player );
	}
}