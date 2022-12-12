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
		long serverTicks = TimeHelper.getServerTicks();
		long delay = 10;
		helper.runAfterDelay( delay, ()->{
			assertThat( helper, serverTicks, TimeHelper.getServerTicks() + delay, ()->"Server delay does count %d ticks delay properly".formatted( delay ) );
			helper.succeed();
		} );
	}

	public TimeHelperTests() {
		super( TimeHelperTests.class );
	}
}
