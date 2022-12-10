package com.mlib.tests;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnTestsRegister;

@AutoInstance
public class BaseTest extends GameModifier {
	public static String getFullSimpleName( Class< ? > clazz ) {
		if( clazz.isMemberClass() ) {
			return String.format( "%s.%s", clazz.getEnclosingClass().getSimpleName(), clazz.getSimpleName() );
		}

		return clazz.getSimpleName();
	}

	public BaseTest( Class< ? > clazz ) {
		new OnTestsRegister.Context( data->data.event.register( clazz ) );
	}
}
