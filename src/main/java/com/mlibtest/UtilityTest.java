package com.mlibtest;

import com.mlib.MajruszLibrary;
import com.mlib.Utility;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class UtilityTest {
	public static void require( boolean condition, String message ) {
		if( !condition )
			throw new GameTestAssertException( message );
	}

	public static void require( boolean condition ) {
		require( condition, "Test failed!" );
	}

	@GameTest( template = "time_converting" )
	public static void convertTime( GameTestHelper helper ) {
		helper.succeedIf( () -> {
			require( Utility.secondsToTicks( 1.5 ) == 30 );
			require( Utility.ticksToSeconds( 30 ) == 1.5 );
			require( Utility.minutesToTicks( 2.0 ) == 2400 );
			require( Utility.ticksToMinutes( 2400 ) == 2.0 );
		} );
	}
}
