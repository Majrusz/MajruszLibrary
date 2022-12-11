package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.GameModifier;
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
		Contexts.getInstances()
			.forEach( contexts->contexts.getContexts()
				.forEach( context->{
					MutableInt max = new MutableInt( Integer.MIN_VALUE );
					context.getConditions()
						.forEach( condition->{
							int priority = condition.getParams().getPriorityAsInt();
							assertThat( helper, priority < max.getValue(), ()->String.format( "%s has invalid priority in %s", getClassName( condition ), getClassName( context ) ) );
							max.setValue( priority );
						} );
				} )
			);
		helper.succeed();
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void conditionValidParameters( GameTestHelper helper ) {
		Contexts.getInstances()
			.forEach( contexts->contexts.getContexts()
				.forEach( context->context.getConditions()
					.forEach( condition->{
						boolean isConfigurable = condition.getParams().isConfigurable();
						boolean doesNotHaveConfigs = condition.getConfigs().isEmpty();
						assertThat( helper, !( isConfigurable && doesNotHaveConfigs ), ()->String.format( "%s is marked configurable even though it does not have any config", getClassName( condition ) ) );
					} ) ) );

		Condition< ? > condition = new Condition<>() {
			@Override
			public boolean check( GameModifier feature, ContextData data ) {
				return true;
			}
		};
		assertThat( helper, condition.isMet( null, null ), ()->String.format( "%s does not return expected result", getClassName( condition ) ) );

		condition.negate();
		assertThat( helper, !condition.isMet( null, null ), ()->String.format( "%s does not return expected negated result", getClassName( condition ) ) );

		helper.succeed();
	}

	public ConditionTests() {
		super( ConditionTests.class );
	}
}
