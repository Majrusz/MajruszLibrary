package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.time.TimeHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class TimeHelperTests extends BaseTest {
	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void timeHelperTicks( GameTestHelper helper ) {
		long ticks = TimeHelper.getServerTicks();
		long delay = 11;
		helper.runAtTickTime( delay, ()->{
			assertThat( helper, ticks + delay, TimeHelper.getServerTicks(), ()->"Server ticks do not handle %d ticks delay properly".formatted( delay ) );
			helper.succeed();
		} );
	}

	public TimeHelperTests() {
		super( TimeHelperTests.class );
	}
}
