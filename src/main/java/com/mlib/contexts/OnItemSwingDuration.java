package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnItemSwingDuration {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( LivingEntity entity, int swingDuration ) {
		return Contexts.get( Data.class ).dispatch( new Data( entity, swingDuration ) );
	}

	public static class Data implements IEntityData {
		public final LivingEntity entity;
		public final int swingDuration;
		public int extraDuration = 0;

		public Data( LivingEntity entity, int swingDuration ) {
			this.entity = entity;
			this.swingDuration = swingDuration;
		}

		@Override
		public Entity getEntity() {
			return this.entity;
		}

		public int getTotalSwingDuration() {
			return Mth.clamp( this.swingDuration + this.extraDuration, 1, 100 );
		}
	}
}