package com.majruszlibrary.time;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.collection.CollectionHelper;
import com.majruszlibrary.events.OnServerTicked;
import com.majruszlibrary.events.base.Priority;
import com.majruszlibrary.platform.Side;
import com.majruszlibrary.time.delays.Delay;
import com.majruszlibrary.time.delays.IDelayedExecution;
import com.majruszlibrary.time.delays.Slider;
import com.majruszlibrary.time.delays.Until;
import net.minecraft.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TimeHelper {
	private static final int TICKS_IN_SECOND = 20;
	private static final int TICKS_IN_MINUTE = TICKS_IN_SECOND * 60;
	private static final List< IDelayedExecution > PENDING_EXECUTIONS = new ArrayList<>();
	private static final List< IDelayedExecution > EXECUTIONS = new ArrayList<>();
	private static long TICKS_COUNTER = -1L;

	static {
		OnServerTicked.listen( TimeHelper::update )
			.priority( Priority.HIGHEST );
	}

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
		PENDING_EXECUTIONS.add( exec );

		return exec;
	}

	public static int toTicks( double seconds ) {
		return ( int )( seconds * TICKS_IN_SECOND );
	}

	public static double toSeconds( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_SECOND;
	}

	public static boolean haveTicksPassed( int ticks ) {
		return TICKS_COUNTER % ticks == 0;
	}

	public static boolean haveSecondsPassed( double seconds ) {
		return TimeHelper.haveTicksPassed( TimeHelper.toTicks( seconds ) );
	}

	public static long getTicks() {
		return TICKS_COUNTER;
	}

	@OnlyIn( Dist.CLIENT )
	public static float getClientTime() {
		return Util.getMillis() / 1000.0f;
	}

	@OnlyIn( Dist.CLIENT )
	public static float getPartialTicks() {
		return Side.getMinecraft().getFrameTime();
	}

	private static void update( OnServerTicked data ) {
		TICKS_COUNTER++;

		List< IDelayedExecution > copy = CollectionHelper.pop( PENDING_EXECUTIONS, ArrayList::new );
		copy.forEach( IDelayedExecution::start );
		EXECUTIONS.addAll( copy );
		for( Iterator< IDelayedExecution > iterator = EXECUTIONS.iterator(); iterator.hasNext(); ) {
			IDelayedExecution exec = iterator.next();
			exec.tick();
			if( exec.isFinished() ) {
				exec.finish();
				iterator.remove();
			}
		}
	}
}
