package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.text.TextHelper;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.function.Function;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class TextHelperTests extends BaseTest {
	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void romanLetters( GameTestHelper helper ) {
		String name = "TextHelper.toRoman()";
		assertThat( helper, "I", 1, TextHelper::toRoman, name );
		assertThat( helper, "IV", 4, TextHelper::toRoman, name );
		assertThat( helper, "XI", 11, TextHelper::toRoman, name );
		assertThat( helper, "XVIII", 18, TextHelper::toRoman, name );
		assertThat( helper, "XCIX", 99, TextHelper::toRoman, name );
		assertThat( helper, "CLV", 155, TextHelper::toRoman, name );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void minPrecision( GameTestHelper helper ) {
		String name = "TextHelper.minPrecision()";
		assertThat( helper, "1", 1.0f, TextHelper::minPrecision, name );
		assertThat( helper, "3.9", 3.900f, TextHelper::minPrecision, name );
		assertThat( helper, "5", 5.001f, TextHelper::minPrecision, name );
		assertThat( helper, "7", 7.00009f, TextHelper::minPrecision, name );
		assertThat( helper, "9.91", 9.9099f, TextHelper::minPrecision, name );
		assertThat( helper, "11.11", 11.11f, TextHelper::minPrecision, name );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void signed( GameTestHelper helper ) {
		String name = "TextHelper.signed()";
		assertThat( helper, "+1", 1.0f, TextHelper::signed, name );
		assertThat( helper, "+0", 0.0f, TextHelper::signed, name );
		assertThat( helper, "-1.01", -1.01f, TextHelper::signed, name );
		assertThat( helper, "+1", 1, TextHelper::signed, name );
		assertThat( helper, "+0", 0, TextHelper::signed, name );
		assertThat( helper, "-1", -1, TextHelper::signed, name );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void signedPercent( GameTestHelper helper ) {
		String name = "TextHelper.signedPercent()";
		assertThat( helper, "+101%", 1.01f, TextHelper::signedPercent, name );
		assertThat( helper, "+1.1%", 0.011f, TextHelper::signedPercent, name );
		assertThat( helper, "-200%", -1.999999f, TextHelper::signedPercent, name );
		assertThat( helper, "+100%", 1, TextHelper::signedPercent, name );
		assertThat( helper, "+0%", 0, TextHelper::signedPercent, name );
		assertThat( helper, "-100%", -1, TextHelper::signedPercent, name );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void percent( GameTestHelper helper ) {
		String name = "TextHelper.percent()";
		assertThat( helper, "99%", 0.99f, TextHelper::percent, name );
		assertThat( helper, "0.01%", 0.0001f, TextHelper::percent, name );
		assertThat( helper, "-100%", -1, TextHelper::percent, name );

		helper.succeed();
	}

	private static < Type extends Number > void assertThat( GameTestHelper helper, String text, Type value,
		Function< Type, String > function, String functionName
	) {
		assertThat( helper, text, function.apply( value ), ()->"%s does not give proper output".formatted( functionName ) );
	}

	public TextHelperTests() {
		super( TextHelperTests.class );
	}
}
