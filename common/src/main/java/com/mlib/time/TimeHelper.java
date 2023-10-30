package com.mlib.time;

import com.mlib.annotation.AutoInstance;
import com.mlib.collection.CollectionHelper;
import com.mlib.contexts.OnClientTicked;
import com.mlib.contexts.OnServerTicked;
import com.mlib.contexts.base.Priority;
import com.mlib.platform.LogicalSafe;
import com.mlib.platform.Side;
import com.mlib.time.delays.Delay;
import com.mlib.time.delays.IDelayedExecution;
import com.mlib.time.delays.Slider;
import com.mlib.time.delays.Until;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TimeHelper {
	private static final int TICKS_IN_SECOND = 20;
	private static final int TICKS_IN_MINUTE = TICKS_IN_SECOND * 60;
	private static final LogicalSafe< Long > COUNTER = LogicalSafe.of( ()->-1L );
	private static final LogicalSafe< List< IDelayedExecution > > PENDING_EXECUTIONS = LogicalSafe.of( ArrayList::new );
	private static final LogicalSafe< List< IDelayedExecution > > EXECUTIONS = LogicalSafe.of( ArrayList::new );

	public static Delay delay( int ticks, Consumer< Delay > callback ) {
		return TimeHelper.run( new Delay( callback, ticks ) );
	}

	public static Delay delay( double seconds, Consumer< Delay > callback ) {
		return TimeHelper.delay( TimeHelper.toTicks( seconds ), callback );
	}

	public static Delay nextTick( Consumer< Delay > callback ) {
		return TimeHelper.delay( 1, callback );
	}

	public static Slider slider( int ticks, Consumer< Slider > callback ) {
		return TimeHelper.run( new Slider( callback, ticks ) );
	}

	public static Slider slider( double seconds, Consumer< Slider > callback ) {
		return TimeHelper.slider( TimeHelper.toTicks( seconds ), callback );
	}

	public static Until until( Predicate< Until > predicate, Consumer< Until > callback ) {
		return TimeHelper.run( new Until( callback, predicate ) );
	}

	public static < Type extends IDelayedExecution > Type run( Type exec ) {
		PENDING_EXECUTIONS.run( list->list.add( exec ) );

		return exec;
	}

	public static int toTicks( double seconds ) {
		return ( int )( seconds * TICKS_IN_SECOND );
	}

	public static double toSeconds( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_SECOND;
	}

	public static boolean haveTicksPassed( int ticks ) {
		return COUNTER.get() % ticks == 0;
	}

	public static boolean haveSecondsPassed( double seconds ) {
		return TimeHelper.haveTicksPassed( TimeHelper.toTicks( seconds ) );
	}

	public static long getTicks() {
		return COUNTER.get();
	}

	public static float getPartialTicks() {
		return Side.get( ()->()->Side.getMinecraft().getFrameTime(), ()->()->0.0f );
	}

	@AutoInstance
	public static class Updater {
		public Updater() {
			OnClientTicked.listen( data->this.update() )
				.priority( Priority.HIGHEST );

			OnServerTicked.listen( data->this.update() )
				.priority( Priority.HIGHEST );
		}

		private void update() {
			COUNTER.set( x->x + 1 );

			PENDING_EXECUTIONS.run( pending->{
				List< IDelayedExecution > copy = CollectionHelper.pop( pending, ArrayList::new );
				copy.forEach( IDelayedExecution::start );
				EXECUTIONS.run( executions->executions.addAll( copy ) );
			} );
			for( Iterator< IDelayedExecution > iterator = EXECUTIONS.get().iterator(); iterator.hasNext(); ) {
				IDelayedExecution exec = iterator.next();
				exec.tick();
				if( exec.isFinished() ) {
					exec.finish();
					iterator.remove();
				}
			}
		}
	}
}
