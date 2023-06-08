package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IProfilerData;
import com.mlib.gamemodifiers.data.ITickData;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnServerTick {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onServerTick( TickEvent.ServerTickEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ITickData, IProfilerData {
		public final TickEvent.ServerTickEvent event;

		public Data( TickEvent.ServerTickEvent event ) {
			this.event = event;
		}

		@Override
		public TickEvent getTickEvent() {
			return this.event;
		}

		@Override
		public ProfilerFiller getProfiler() {
			return ServerLifecycleHooks.getCurrentServer().getProfiler();
		}
	}
}