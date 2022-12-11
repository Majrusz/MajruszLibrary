package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.text.TextHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class TextHelperTests extends BaseTest {
	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void romanLetters( GameTestHelper helper ) {
		assertThatRomanLetter( helper, "I", 1 );
		assertThatRomanLetter( helper, "IV", 4 );
		assertThatRomanLetter( helper, "XI", 11 );
		assertThatRomanLetter( helper, "XVIII", 18 );
		assertThatRomanLetter( helper, "XCIX", 99 );
		assertThatRomanLetter( helper, "CLV", 155 );

		helper.succeed();
	}

	private static void assertThatRomanLetter( GameTestHelper helper, String roman, int decimal ) {
		assertThat( helper, roman.equals( TextHelper.toRoman( decimal ) ), ()->"TextHelper.toRoman() does not give proper output for %d".formatted( decimal ) );
	}

	@PrefixGameTestTemplate( false )
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void minPrecision( GameTestHelper helper ) {
		assertThatMinPrecision( helper, "1", 1.0f );
		assertThatMinPrecision( helper, "3.9",  3.900f );
		assertThatMinPrecision( helper, "5.001",  5.001f );
		assertThatMinPrecision( helper, "7",  7.00009f );
		assertThatMinPrecision( helper, "9.909",  9.909f );
		assertThatMinPrecision( helper, "11.11",  11.11f );

		helper.succeed();
	}

	private static void assertThatMinPrecision( GameTestHelper helper, String text, float value ) {
		assertThat( helper, text.equals( TextHelper.minPrecision( value ) ), ()->"TextHelper.minPrecision() does not give proper output for %f".formatted( value ) );
	}

	public TextHelperTests() {
		super( TextHelperTests.class );
	}
}
