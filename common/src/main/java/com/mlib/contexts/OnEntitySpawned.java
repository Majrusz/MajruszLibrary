package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class OnEntitySpawned {
	public static final Consumer< Data > CANCEL = Data::cancelSpawn;

	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Entity entity, boolean isLoadedFromDisk ) {
		return Contexts.get( Data.class ).dispatch( new Data( entity, isLoadedFromDisk ) );
	}

	public static class Data implements IEntityData {
		public final Entity entity;
		public final boolean isLoadedFromDisk;
		public final BlockPos position;
		public final DifficultyInstance difficulty;
		private boolean isSpawnCancelled = false;

		public Data( Entity entity, boolean isLoadedFromDisk ) {
			this.entity = entity;
			this.isLoadedFromDisk = isLoadedFromDisk;
			this.position = entity.blockPosition();
			this.difficulty = entity.level().getCurrentDifficultyAt( this.position );
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}

		public void cancelSpawn() {
			this.isSpawnCancelled = true;
		}

		public boolean isSpawnCancelled() {
			return this.isSpawnCancelled;
		}
	}
}
