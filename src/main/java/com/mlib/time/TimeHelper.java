package com.mlib.time;

import com.mlib.Utility;
import com.mlib.annotations.AutoInstance;
import com.mlib.config.DoubleConfig;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Priority;
import com.mlib.gamemodifiers.contexts.OnClientTick;
import com.mlib.gamemodifiers.contexts.OnServerTick;

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
