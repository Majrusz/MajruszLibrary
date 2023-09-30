package net.mlib.contexts;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.mlib.contexts.base.Context;
import net.mlib.contexts.base.Contexts;
import net.mlib.contexts.data.ILevelData;

import java.util.function.Consumer;

public class OnBabySpawned {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( Animal parentA, Animal parentB, Player player, AgeableMob child ) {
		return Contexts.get( Data.class ).dispatch( new Data( parentA, parentB, player, child ) );
	}

	public static class Data implements ILevelData {
		public final Animal parentA;
		public final Animal parentB;
		public final Player player;
		public final AgeableMob child;

		public Data( Animal parentA, Animal parentB, Player player, AgeableMob child ) {
			this.parentA = parentA;
			this.parentB = parentB;
			this.player = player;
			this.child = child;
		}

		@Override
		public Level getLevel() {
			return this.child != null ? this.child.level() : null;
		}
	}
}
