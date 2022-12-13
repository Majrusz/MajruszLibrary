package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.time.TimeHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class TimeHelperTests extends BaseTest {
	static final long DELAY = 6;

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void timeHelperTicks( GameTestHelper helper ) {
		long ticks = TimeHelper.getServerTicks();
		helper.runAfterDelay( DELAY, ()->{
			assertThat( helper, ticks + DELAY, TimeHelper.getServerTicks(), ()->"Server ticks are not handled properly" );
			helper.succeed();
		} );
	}

	public TimeHelperTests() {
		super( TimeHelperTests.class );
	}
}
