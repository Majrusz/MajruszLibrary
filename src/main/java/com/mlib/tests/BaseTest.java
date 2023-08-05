package com.mlib.tests;

import com.mlib.contexts.OnTestsRegistered;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.Supplier;

public class BaseTest {
	public static String getClassName( Object object ) {
		Class< ? > clazz = object.getClass();
		if( clazz.isMemberClass() ) {
			return String.format( "%s.%s", clazz.getEnclosingClass().getSimpleName(), clazz.getSimpleName() );
		}

		return clazz.getSimpleName();
	}

	public static int getTickCount( GameTestHelper helper ) {
		return helper.getLevel().getServer().getTickCount();
	}

	public static void assertThat( GameTestHelper helper, boolean condition, Supplier< String > message ) {
		if( !condition ) {
			helper.fail( message.get() );
		}
	}

	public static < Type > void assertThat( GameTestHelper helper, Type result, Type expected, Supplier< String > message ) {
		if( !result.equals( expected ) ) {
			helper.fail( "%s (result: %s, expected: %s)".formatted( message.get(), result, expected ) );
		}
	}

	public BaseTest( Class< ? > clazz ) {
		OnTestsRegistered.listen( data->data.event.register( clazz ) );
	}
}
