package com.mlib.time;

import com.mlib.Utility;
import com.mlib.gamemodifiers.contexts.OnServerTick;
import net.minecraft.util.Mth;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public class Slider {
	static final List< Slider > PENDING_SLIDERS = new ArrayList<>();
	static final List< Slider > SLIDERS = new ArrayList<>();

	static {
		new OnServerTick.Context( Slider::update )
			.addCondition( data->data.event.phase != TickEvent.Phase.START );
	}

	int ticksLeft;
	final int ticksTotal;
	final ICallable callable;

	public Slider( int ticks, ICallable callable ) {
		this.ticksLeft = ticks;
		this.ticksTotal = ticks;
		this.callable = callable;

		PENDING_SLIDERS.add( this );
	}

	public Slider( double seconds, ICallable callable ) {
		this( Utility.secondsToTicks( seconds ), callable );
	}

	public float getRatio() {
		return 1.0f - this.getRatioLeft();
	}

	public float getRatioLeft() {
		return Mth.clamp( 1.0f * this.ticksLeft / this.ticksTotal, 0.0f, 1.0f );
	}

	public int getTicksLeft() {
		return this.ticksLeft;
	}

	public int getTicksTotal() {
		return this.ticksTotal;
	}

	public boolean isFinished() {
		return this.ticksLeft <= 0;
	}

	private void call() {
		this.callable.call( this );
	}

	private static void update( OnServerTick.Data data ) {
		SLIDERS.addAll( PENDING_SLIDERS );
		PENDING_SLIDERS.clear();
		for( Iterator< Slider > iterator = SLIDERS.iterator(); iterator.hasNext(); ) {
			Slider slider = iterator.next();
			slider.call();
			if( slider.isFinished() ) {
				iterator.remove();
			} else {
				slider.ticksLeft = slider.ticksLeft - 1;
			}
		}
	}

	public interface ICallable {
		void call( Slider slider );
	}
}
