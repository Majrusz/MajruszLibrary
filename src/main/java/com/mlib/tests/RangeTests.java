package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.math.Range;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class RangeTests extends BaseTest {
	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void within( GameTestHelper helper ) {
		assertThat( helper, new Range<>( 1.0f, 1.2f ), 0.9999f, false );
		assertThat( helper, new Range<>( 0.9f, 1.0f ), 0.9999f, true );
		assertThat( helper, new Range<>( -1.0f, 0.2f ), 0.2000f, true );
		assertThat( helper, new Range<>( -1.2f, 0.0f ), -1.2001f, false );

		assertThat( helper, new Range<>( 1.0, 1.2 ), 0.9999, false );
		assertThat( helper, new Range<>( 0.9, 1.0 ), 0.9999, true );
		assertThat( helper, new Range<>( -1.0, 0.2 ), 0.2000, true );
		assertThat( helper, new Range<>( -1.2, 0.0 ), -1.2001, false );

		assertThat( helper, new Range<>( 1, 5 ), 0, false );
		assertThat( helper, new Range<>( 0, 4 ), 3, true );
		assertThat( helper, new Range<>( -5, -1 ), -1, true );
		assertThat( helper, new Range<>( -8, -8 ), -9, false );

		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void clamp( GameTestHelper helper ) {
		assertThat( helper, new Range<>( 1.0f, 1.2f ), 0.9999f, 1.0000f );
		assertThat( helper, new Range<>( 0.9f, 1.0f ), 0.9999f, 0.9999f );
		assertThat( helper, new Range<>( -1.0f, 0.2f ), 0.2000f, 0.2000f );
		assertThat( helper, new Range<>( -1.2f, 0.0f ), -1.2001f, -1.2000f );

		assertThat( helper, new Range<>( 1.0, 1.2 ), 0.9999, 1.0000 );
		assertThat( helper, new Range<>( 0.9, 1.0 ), 0.9999, 0.9999 );
		assertThat( helper, new Range<>( -1.0, 0.2 ), 0.2000, 0.2000 );
		assertThat( helper, new Range<>( -1.2, 0.0 ), -1.2001, -1.2000 );

		assertThat( helper, new Range<>( 1, 5 ), 0, 1 );
		assertThat( helper, new Range<>( 0, 4 ), 3, 3 );
		assertThat( helper, new Range<>( -5, -1 ), -1, -1 );
		assertThat( helper, new Range<>( -8, -8 ), -9, -8 );

		helper.succeed();
	}

	private static < Type extends Number & Comparable< Type > > void assertThat( GameTestHelper helper, Range< Type > range, Type value, boolean expected ) {
		boolean result = range.within( value );
		assertThat( helper, result == expected, ()->"Range.within() returns invalid result (result: %s, expected: %s)".formatted( result, expected ) );
	}

	private static < Type extends Number & Comparable< Type > > void assertThat( GameTestHelper helper, Range< Type > range, Type value, Type expected ) {
		Type result = range.clamp( value );
		assertThat( helper, result.equals( expected ), ()->"Range.clamp() returns invalid result (result: %s, expected: %s)".formatted( result, expected ) );
	}

	public RangeTests() {
		super( RangeTests.class );
	}
}
