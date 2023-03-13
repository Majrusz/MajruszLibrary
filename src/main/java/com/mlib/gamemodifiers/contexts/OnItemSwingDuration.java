package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnItemSwingDuration {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( LivingEntity entity, int swingDuration ) {
		return Contexts.get( Data.class ).dispatch( new Data( entity, swingDuration ) );
	}

	public static class Data implements ILevelData {
		public final LivingEntity entity;
		public final int swingDuration;
		public int extraDuration = 0;

		public Data( LivingEntity entity, int swingDuration ) {
			this.entity = entity;
			this.swingDuration = swingDuration;
		}

		@Override
		public Level getLevel() {
			return this.entity.getLevel();
		}

		public int getTotalSwingDuration() {
			return Mth.clamp( this.swingDuration + this.extraDuration, 1, 100 );
		}
	}
}