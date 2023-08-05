package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.config.IConfigurable;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Contexts;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.commons.lang3.mutable.MutableInt;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class ConditionTests extends BaseTest {
	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void priority( GameTestHelper helper ) {
		Contexts.get()
			.forEach( contexts->contexts
				.forEach( context->{
					MutableInt max = new MutableInt( Integer.MIN_VALUE );
					context.getConditions()
						.forEach( condition->{
							int priority = condition.getPriority().ordinal();
							assertThat( helper, priority >= max.getValue(), ()->"%s has invalid priority in %s".formatted( getClassName( condition ), getClassName( context ) ) );
							max.setValue( priority );
						} );
				} )
			);
		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void validParameters( GameTestHelper helper ) {
		Contexts.get()
			.forEach( contexts->contexts
				.forEach( context->context.getConditions()
					.forEach( condition->{
						boolean isConfigurable = condition.isConfigurable();
						boolean doesNotHaveConfigs = condition.getConfigs().isEmpty();
						assertThat( helper, !( isConfigurable && doesNotHaveConfigs ), ()->"%s is marked configurable even though it does not have any config".formatted( getClassName( condition ) ) );
					} ) ) );

		Condition< ? > condition = new Condition<>( data->true );
		assertThat( helper, condition.check( null ), ()->"%s does not return expected result".formatted( getClassName( condition ) ) );

		condition.negate();
		assertThat( helper, !condition.check( null ), ()->"%s does not return expected negated result".formatted( getClassName( condition ) ) );

		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void builtConfigs( GameTestHelper helper ) {
		Contexts.get()
			.forEach( contexts->contexts
				.forEach( context->context.getConditions()
					.forEach( condition->{
						boolean isConfigurable = condition.isConfigurable();
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
