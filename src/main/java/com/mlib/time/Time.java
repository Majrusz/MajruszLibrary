package com.mlib.time;

import com.mlib.Utility;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnServerTick;
import com.mlib.gamemodifiers.Priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Time {
	static final List< ISuspendedExecution > PENDING_EXECUTIONS = Collections.synchronizedList( new ArrayList<>() );
	static final List< ISuspendedExecution > EXECUTIONS = Collections.synchronizedList( new ArrayList<>() );

	public static Delay delay( int ticks, Consumer< Delay > callback ) {
		return setup( new Delay( callback, ticks ) );
	}

	public static Delay delay( double seconds, Consumer< Delay > callback ) {
		return delay( Utility.secondsToTicks( seconds ), callback );
	}

	public static Delay nextTick( Runnable callback ) {
		return delay( 1, delay->callback.run() );
	}

	public static Slider slider( int ticks, Consumer< Slider > callback ) {
		return setup( new Slider( callback, ticks ) );
	}

	public static Slider slider( double seconds, Consumer< Slider > callback ) {
		return slider( Utility.secondsToTicks( seconds ), callback );
	}

	public static Until until( Supplier< Boolean > isReady, Consumer< Until > callback ) {
		return setup( new Until( callback, isReady ) );
	}

	public static < Type extends ISuspendedExecution > Type setup( Type exec ) {
		PENDING_EXECUTIONS.add( exec );

		return exec;
	}

	@AutoInstance
	public static class Updater extends GameModifier {
		public Updater() {
			new OnServerTick.Context( this::updateExecs )
				.priority( Priority.HIGHEST )
				.addCondition( TimeHelper::isEndPhase )
				.insertTo( this );
		}

		private void updateExecs( OnServerTick.Data data ) {
			synchronized( PENDING_EXECUTIONS ) {
				PENDING_EXECUTIONS.forEach( ISuspendedExecution::onStart );
				EXECUTIONS.addAll( PENDING_EXECUTIONS );
				PENDING_EXECUTIONS.clear();
			}
			for( Iterator< ISuspendedExecution > iterator = EXECUTIONS.iterator(); iterator.hasNext(); ) {
				ISuspendedExecution exec = iterator.next();
				exec.onTick();
				if( exec.isFinished() ) {
					exec.onEnd();
					iterator.remove();
				}
			}
		}
	}
}
