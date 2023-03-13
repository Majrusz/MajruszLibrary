package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import com.mlib.gamemodifiers.data.ITickData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerTick {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onPlayerTick( TickEvent.PlayerTickEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData, ITickData {
		public final TickEvent.PlayerTickEvent event;
		public final Player player;

		public Data( TickEvent.PlayerTickEvent event ) {
			this.event = event;
			this.player = event.player;
		}

		@Override
		public Level getLevel() {
			return this.player.getLevel();
		}

		@Override
		public TickEvent getTickEvent() {
			return this.event;
		}
	}
}
