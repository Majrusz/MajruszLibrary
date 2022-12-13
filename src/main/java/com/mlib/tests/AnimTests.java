package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.time.Anim;
import com.mlib.time.Delay;
import com.mlib.time.IAnimation;
import com.mlib.time.Slider;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class AnimTests extends BaseTest {
	static final int DELAY = 4;

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void animInterface( GameTestHelper helper ) {
		var anim = new IAnimation() {
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
		Anim.setup( anim );

		helper.failIfEver( ()->{
			if( !anim.isFinished() )
				return;

			assertThat( helper, anim.endTick - anim.startTick + 1, DELAY, ()->"Anim does not handle ticks properly" );
			helper.succeed();
		} );
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void animDelay( GameTestHelper helper ) {
		Delay delay = Anim.delay( _delay->{}, DELAY );
		int startTick = getTickCount( helper );

		helper.failIfEver( ()->{
			if( !delay.isFinished() )
				return;

			int endTick = getTickCount( helper );
			assertThat( helper, endTick - startTick, DELAY, ()->"Delay does not handle ticks properly" );
			helper.succeed();
		} );
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void animSlider( GameTestHelper helper ) {
		AtomicInteger counter = new AtomicInteger( 0 );
		Slider slider = Anim.slider( _delay->counter.set( counter.get() + 1 ), DELAY );
		int startTick = getTickCount( helper );

		helper.failIfEver( ()->{
			if( !slider.isFinished() )
				return;

			int endTick = getTickCount( helper );
			assertThat( helper, endTick - startTick, DELAY, ()->"Slider does not handle ticks properly" );
			assertThat( helper, counter.get(), DELAY, ()->"Slider callback should be called as many times as the delay ticks" );
			helper.succeed();
		} );
	}

	public AnimTests() {
		super( AnimTests.class );
	}
}
