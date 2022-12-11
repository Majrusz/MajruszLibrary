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
		assertThat( helper, roman.equals( TextHelper.toRoman( decimal ) ), ()->"TextHelper.toRoman() does not give proper output" );
	}

	public TextHelperTests() {
		super( TextHelperTests.class );
	}
}
