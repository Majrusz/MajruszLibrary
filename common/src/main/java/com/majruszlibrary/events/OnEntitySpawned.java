package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ICancellableEvent;
import com.majruszlibrary.events.type.IEntityEvent;
import com.majruszlibrary.mixininterfaces.IMixinMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntitySpawned implements ICancellableEvent, IEntityEvent {
	public final Entity entity;
	public final boolean isLoadedFromDisk;
	public final BlockPos position;
	public final DifficultyInstance difficulty;
	public final @Nullable MobSpawnType mobSpawnType;
	private boolean isSpawnCancelled = false;

	public static Event< OnEntitySpawned > listen( Consumer< OnEntitySpawned > consumer ) {
		return Events.get( OnEntitySpawned.class ).add( consumer );
	}

	public OnEntitySpawned( Entity entity, boolean isLoadedFromDisk ) {
		this.entity = entity;
		this.isLoadedFromDisk = isLoadedFromDisk;
		this.position = entity.blockPosition();
		this.difficulty = entity.getLevel().getCurrentDifficultyAt( this.position );
		this.mobSpawnType = entity instanceof IMixinMob mob ? mob.getMobSpawnType() : null;
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
