package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnAnimalTamed {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Animal animal, Player player ) {
		return Contexts.get( Data.class ).dispatch( new Data( animal, player ) );
	}

	public static class Data implements IEntityData {
		public final Animal animal;
		public final Player tamer;

		public Data( Animal animal, Player player ) {
			this.animal = animal;
			this.tamer = player;
		}

		@Override
		public Entity getEntity() {
			return this.animal;
		}
	}
}
