package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.time.Anim;
import com.mlib.time.IAnimation;
import com.mlib.time.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class AnimTests extends BaseTest {
	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void animInterface( GameTestHelper helper ) {
		final int animTicks = 3;
		var anim = new IAnimation() {
			public int startTick;
			public int ticksLeft = animTicks;
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

			assertThat( helper, anim.endTick - anim.startTick + 1, animTicks, ()->"Anims do not handle ticks properly" );
			helper.succeed();
		} );
	}

	public AnimTests() {
		super( AnimTests.class );
	}
}
