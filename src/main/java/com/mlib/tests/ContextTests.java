package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import org.apache.commons.lang3.mutable.MutableInt;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class ContextTests extends BaseTest {
	@GameTest( templateNamespace = "mlib", template = "empty" )
	public static void priority( GameTestHelper helper ) {
		Contexts.getInstances()
			.forEach( contexts->{
				MutableInt max = new MutableInt( Integer.MIN_VALUE );
				contexts.getContexts()
					.forEach( context->{
						int priority = context.getParams().getPriorityAsInt();
						assertThat( helper, priority >= max.getValue(), ()->"%s has invalid priority".formatted( getClassName( context ) ) );
						max.setValue( priority );
					} );
			} );
		helper.succeed();
	}

	public ContextTests() {
		super( ContextTests.class );
	}
}
