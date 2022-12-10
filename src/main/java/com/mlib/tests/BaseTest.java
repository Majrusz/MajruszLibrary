package com.mlib.tests;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnTestsRegister;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.Supplier;

@AutoInstance
public class BaseTest extends GameModifier {
	public static String getClassName( Object object ) {
		Class< ? > clazz = object.getClass();
		if( clazz.isMemberClass() ) {
			return String.format( "%s.%s", clazz.getEnclosingClass().getSimpleName(), clazz.getSimpleName() );
		}

		return clazz.getSimpleName();
	}

	public static void assertThat( GameTestHelper helper, boolean condition, Supplier< String > message ) {
		if( condition ) {
			helper.fail( message.get() );
		}
	}

	public BaseTest( Class< ? > clazz ) {
		new OnTestsRegister.Context( data->data.event.register( clazz ) );
	}
}
