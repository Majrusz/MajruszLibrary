package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.config.IConfigurable;
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
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void priority( GameTestHelper helper ) {
		Contexts.getInstances()
			.forEach( contexts->contexts.getContexts()
				.forEach( context->{
					MutableInt max = new MutableInt( Integer.MIN_VALUE );
					context.getConditions()
						.forEach( condition->{
							int priority = condition.getParams().getPriorityAsInt();
							assertThat( helper, priority >= max.getValue(), ()->"%s has invalid priority in %s".formatted( getClassName( condition ), getClassName( context ) ) );
							max.setValue( priority );
						} );
				} )
			);
		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void validParameters( GameTestHelper helper ) {
		Contexts.getInstances()
			.forEach( contexts->contexts.getContexts()
				.forEach( context->context.getConditions()
					.forEach( condition->{
						boolean isConfigurable = condition.getParams().isConfigurable();
						boolean doesNotHaveConfigs = condition.getConfigs().isEmpty();
						assertThat( helper, !( isConfigurable && doesNotHaveConfigs ), ()->"%s is marked configurable even though it does not have any config".formatted( getClassName( condition ) ) );
					} ) ) );

		Condition< ? > condition = new Condition<>() {
			@Override
			public boolean check( GameModifier feature, ContextData data ) {
				return true;
			}
		};
		assertThat( helper, condition.isMet( null, null ), ()->"%s does not return expected result".formatted( getClassName( condition ) ) );

		condition.negate();
		assertThat( helper, !condition.isMet( null, null ), ()->"%s does not return expected negated result".formatted( getClassName( condition ) ) );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void builtConfigs( GameTestHelper helper ) {
		Contexts.getInstances()
			.forEach( contexts->contexts.getContexts()
				.forEach( context->context.getConditions()
					.forEach( condition->{
						boolean isConfigurable = condition.getParams().isConfigurable();
						boolean isConditionBuilt = condition.isBuilt();
						boolean areConditionConfigsBuilt = condition.getConfigs().stream().allMatch( IConfigurable::isBuilt );
						assertThat( helper, !isConfigurable || isConditionBuilt && areConditionConfigsBuilt, ()->"Some of the configs in %s has not been built".formatted( getClassName( condition ) ) );
					} ) ) );

		helper.succeed();
	}

	public ConditionTests() {
		super( ConditionTests.class );
	}
}
