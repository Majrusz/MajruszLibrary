package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnTestsRegister;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.commons.lang3.mutable.MutableInt;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class LibTests {
	@GameTest( template = "empty" )
	public static void contextPriority( GameTestHelper helper ) {
		Contexts.getInstances().forEach( contexts->{
			MutableInt max = new MutableInt( Integer.MIN_VALUE );
			contexts.forEach( context->{
				int priority = context.getParams().getPriorityAsInt();
				if( priority < max.getValue() ) {
					helper.fail( String.format( "%s has invalid priority", getFullSimpleName( context.getClass() ) ) );
				}
				max.setValue( priority );
			} );
		} );
		helper.succeed();
	}

	@GameTest( template = "empty" )
	public static void conditionPriority( GameTestHelper helper ) {
		Contexts.getInstances().forEach( contexts->contexts.forEach( context->{
			MutableInt max = new MutableInt( Integer.MIN_VALUE );
			context.getConditions().forEach( condition->{
				int priority = condition.getParams().getPriorityAsInt();
				if( priority < max.getValue() ) {
					helper.fail( String.format( "%s has invalid priority in %s", getFullSimpleName( condition.getClass() ), getFullSimpleName( context.getClass() ) ) );
				}
				max.setValue( priority );
			} );
		} ) );
		helper.succeed();
	}

	public static String getFullSimpleName( Class< ? > clazz ) {
		if( clazz.isMemberClass() ) {
			return String.format( "%s.%s", clazz.getEnclosingClass().getSimpleName(), clazz.getSimpleName() );
		}

		return clazz.getSimpleName();
	}

	@AutoInstance
	public static class Register extends GameModifier {
		public Register() {
			new OnTestsRegister.Context( data->data.event.register( LibTests.class ) );
		}
	}
}
