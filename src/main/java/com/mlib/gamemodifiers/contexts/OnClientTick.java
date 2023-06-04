package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IProfilerData;
import com.mlib.gamemodifiers.data.ITickData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnClientTick {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onClientTick( TickEvent.ClientTickEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ITickData, IProfilerData {
		public final TickEvent.ClientTickEvent event;

		public Data( TickEvent.ClientTickEvent event ) {
			this.event = event;
		}

		@Override
		public TickEvent getTickEvent() {
			return this.event;
		}

		@Override
		public ProfilerFiller getProfiler() {
			return DistExecutor.unsafeCallWhenOn( Dist.CLIENT, ()->()->Minecraft.getInstance().getProfiler() );
		}
	}
}