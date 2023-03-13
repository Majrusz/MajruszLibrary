package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnBabySpawn {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onBreed( BabyEntitySpawnEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData {
		public final BabyEntitySpawnEvent event;
		public final AgeableMob child;
		public final Mob parentA;
		public final Mob parentB;
		public final Player player;

		public Data( BabyEntitySpawnEvent event ) {
			this.event = event;
			this.child = event.getChild();
			this.parentA = event.getParentA();
			this.parentB = event.getParentB();
			this.player = event.getCausedByPlayer();
		}

		@Override
		public Level getLevel() {
			return this.parentA.getLevel();
		}
	}
}
