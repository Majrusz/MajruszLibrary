package com.mlib.time;

import com.mlib.Utility;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
public class Delay {
	static final List< Delay > PENDING_DELAYS = new ArrayList<>();
	static final List< Delay > DELAYS = new ArrayList<>();

	public static void onNextTick( ICallable callable ) {
		new Delay( 0, callable );
	}

	int ticksLeft;
	final ICallable callable;

	public Delay( int ticks, ICallable callable ) {
		this.ticksLeft = ticks;
		this.callable = callable;

		PENDING_DELAYS.add( this );
	}

	public Delay( double seconds, ICallable callable ) {
		this( Utility.secondsToTicks( seconds ), callable );
	}

	public boolean isAboutToGetCalled() {
		return this.ticksLeft <= 0;
	}

	@SubscribeEvent
	public static void onServerTick( TickEvent.ServerTickEvent event ) {
		if( event.phase != TickEvent.Phase.START ) {
			return;
		}

		DELAYS.addAll( PENDING_DELAYS );
		PENDING_DELAYS.clear();
		for( Iterator< Delay > iterator = DELAYS.iterator(); iterator.hasNext(); ) {
			Delay delay = iterator.next();
			if( delay.isAboutToGetCalled() ) {
				delay.callable.call();
				iterator.remove();
			} else {
				delay.ticksLeft = delay.ticksLeft - 1;
			}
		}
	}

	public interface ICallable {
		void call();
	}
}
