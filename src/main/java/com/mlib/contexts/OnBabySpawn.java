package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
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
		public final Mob parentA;
		public final Mob parentB;
		@Nullable
		public final Player player;
		@Nullable
		public final AgeableMob child;

		public Data( BabyEntitySpawnEvent event ) {
			this.event = event;
			this.parentA = event.getParentA();
			this.parentB = event.getParentB();
			this.player = event.getCausedByPlayer();
			this.child = event.getChild();
		}

		@Override
		public Level getLevel() {
			return this.child != null ? this.child.level() : null;
		}
	}
}
