package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.time.Time;
import com.mlib.time.Delay;
import com.mlib.time.ISuspendedExecution;
import com.mlib.time.Slider;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class SuspendedExecutionTests extends BaseTest {
	static final int DELAY = 4;

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void interfacee( GameTestHelper helper ) {
		var exec = new ISuspendedExecution() {
			public int startTick;
			public int ticksLeft = DELAY;
			public int endTick;

			@Override
			public void onStart() {
				this.startTick = getTickCount( helper );
			}

			@Override
			public void onTick() {
				--this.ticksLeft;
			}

			@Override
			public void onEnd() {
				this.endTick = getTickCount( helper );
			}

			@Override
			public boolean isFinished() {
				return this.ticksLeft == 0;
			}
		};
		Time.setup( exec );

		helper.failIfEver( ()->{
			if( !exec.isFinished() )
				return;

			assertThat( helper, exec.endTick - exec.startTick + 1, DELAY, ()->"Suspended execution does not handle ticks properly" );
			helper.succeed();
		} );
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void delay( GameTestHelper helper ) {
		Delay delay = Time.delay( DELAY, _delay->{} );
		int startTick = getTickCount( helper );

		helper.failIfEver( ()->{
			if( !delay.isFinished() )
				return;

			int endTick = getTickCount( helper );
			assertThat( helper, endTick - startTick, DELAY, ()->"Delay does not handle ticks properly" );
			helper.succeed();
		} );
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void slider( GameTestHelper helper ) {
		AtomicInteger counter = new AtomicInteger( 0 );
		Slider slider = Time.slider( DELAY, _delay->counter.set( counter.get() + 1 ) );
		int startTick = getTickCount( helper );

		helper.failIfEver( ()->{
			if( !slider.isFinished() )
				return;

			int endTick = getTickCount( helper );
			assertThat( helper, endTick - startTick, DELAY, ()->"Slider does not handle ticks properly" );
			assertThat( helper, counter.get(), DELAY + 1, ()->"Slider callback should be called as many times as the delay ticks plus one" );
			helper.succeed();
		} );
	}

	public SuspendedExecutionTests() {
		super( SuspendedExecutionTests.class );
	}
}
