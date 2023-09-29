package net.mlib.contexts;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.mlib.contexts.base.Context;
import net.mlib.contexts.base.Contexts;
import net.mlib.contexts.data.IEntityData;

import java.util.function.Consumer;

public class OnAnimalTamed {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static void dispatch( Animal animal, Player player ) {
		Contexts.get( Data.class ).dispatch( new Data( animal, player ) );
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
