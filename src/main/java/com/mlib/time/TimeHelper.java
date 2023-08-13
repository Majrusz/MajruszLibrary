package com.mlib.time;

import com.mlib.Utility;
import com.mlib.modhelper.AutoInstance;
import com.mlib.config.DoubleConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Priority;
import com.mlib.contexts.OnClientTick;
import com.mlib.contexts.OnServerTick;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnegative;

public class TimeHelper {
	static long clientCounter = 1;
	static long serverCounter = 1;

	public static boolean hasClientTicksPassed( @Nonnegative int ticks ) {
		return clientCounter % ticks == 0;
	}

	public static boolean hasClientSecondsPassed( @Nonnegative double seconds ) {
		return hasClientTicksPassed( Utility.secondsToTicks( seconds ) );
	}

	public static boolean hasClientSecondsPassed( DoubleConfig config ) {
		return hasClientTicksPassed( config.asTicks() );
	}

	public static long getClientTicks() {
		return clientCounter;
	}

	public static boolean hasServerTicksPassed( @Nonnegative int ticks ) {
		return serverCounter % ticks == 0;
	}

	public static boolean hasServerSecondsPassed( @Nonnegative double seconds ) {
		return hasServerTicksPassed( Utility.secondsToTicks( seconds ) );
	}

	public static boolean hasServerSecondsPassed( DoubleConfig config ) {
		return hasServerTicksPassed( config.asTicks() );
	}

	public static long getServerTicks() {
		return serverCounter;
	}

	public static float getPartialTicks() {
		return DistExecutor.unsafeRunForDist( ()->()->Minecraft.getInstance().getFrameTime(), ()->()->0.0f );
	}

	@AutoInstance
	public static class Updater {
		public Updater() {
			OnClientTick.listen( data->++clientCounter )
				.priority( Priority.HIGHEST )
				.addCondition( Condition.isEndPhase() );

			OnServerTick.listen( data->++serverCounter )
				.priority( Priority.HIGHEST )
				.addCondition( Condition.isEndPhase() );
		}
	}
}
