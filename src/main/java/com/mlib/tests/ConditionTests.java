package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import org.apache.commons.lang3.mutable.MutableInt;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class ConditionTests extends BaseTest {
	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void conditionPriority( GameTestHelper helper ) {
		Contexts.getInstances().forEach( contexts->contexts.forEach( context->{
			MutableInt max = new MutableInt( Integer.MIN_VALUE );
			context.getConditions().forEach( condition->{
				int priority = condition.getParams().getPriorityAsInt();
				assertThat( helper, priority < max.getValue(), ()->String.format( "%s has invalid priority in %s", getClassName( condition ), getClassName( context ) ) );
				max.setValue( priority );
			} );
		} ) );
		helper.succeed();
	}

	public ConditionTests() {
		super( ConditionTests.class );
	}
}
