package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.animations.Animation;
import com.mlib.animations.Frame;
import com.mlib.animations.InterpolationType;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.Mth;
import net.minecraftforge.gametest.GameTestHolder;
import com.mojang.math.Vector3f;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class AnimationTests extends BaseTest {
	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void applyFloat( GameTestHelper helper ) {
		Animation< Float > anim = new Animation<>( 2.0f );
		anim.add( 0.0f, new Frame.Value( 0.0f ) )
			.add( 1.0f, new Frame.Value( 5.0f ) )
			.add( 2.0f, new Frame.Value( 20.0f ) );

		assertThat( helper, anim.apply( 0.0f, 0.0f ), 0.0f );
		assertThat( helper, anim.apply( 0.5f, 0.0f ), 2.5f );
		assertThat( helper, anim.apply( 0.5f, 0.5f ), anim.apply( 0.5f, 1.5f ) );
		assertThat( helper, anim.apply( 0.5f, 0.5f ), 2.5625f );
		assertThat( helper, anim.apply( 0.5f, 1.5f ), 2.5625f );
		assertThat( helper, anim.apply( 1.0f, 1.0f ), 5.0f );
		assertThat( helper, anim.apply( 1.5f, 2.0f ), 12.5f );
		assertThat( helper, anim.apply( 2.0f, 2.0f ), 20.0f );
		assertThat( helper, anim.apply( 2.0f, 2.137f ), 20.0f );
		assertThat( helper, anim.apply( 2.5f, 3.0f ), 20.0f );

		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void applyDegrees( GameTestHelper helper ) {
		Animation< Float > anim = new Animation<>( 3.0f );
		anim.add( 0.0f, new Frame.Degrees( 0.0f ) )
			.add( 1.0f, new Frame.Degrees( 30.0f, InterpolationType.SMOOTH ) )
			.add( 2.0f, new Frame.Degrees( 90.0f, InterpolationType.SQUARE ) )
			.add( 3.0f, new Frame.Degrees( 180.0f, InterpolationType.CUBE_ROOT ) );

		assertThat( helper, anim.apply( 0.0f, 0.0f ), Mth.DEG_TO_RAD * 0.0f );
		assertThat( helper, anim.apply( 0.4f, 0.0f ), Mth.DEG_TO_RAD * ( 0.4f * 0.4f * ( 3 - 2 * 0.4f ) ) * 30.0f );
		assertThat( helper, anim.apply( 1.0f, 0.0f ), Mth.DEG_TO_RAD * 30.0f );
		assertThat( helper, anim.apply( 1.5f, 0.0f ), Mth.DEG_TO_RAD * ( 30.0f + 0.5f * 0.5f * ( 90.0f - 30.0f ) ) );
		assertThat( helper, anim.apply( 2.25f, 0.0f ), Mth.DEG_TO_RAD * ( 90.0f + ( float )Math.pow( 0.25, 1.0 / 3.0 ) * ( 180.0f - 90.0f ) ) );
		assertThat( helper, anim.apply( 3.0f, 0.0f ), Mth.DEG_TO_RAD * 180.0f );

		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void applyVector( GameTestHelper helper ) {
		Animation< Vector3f > anim = new Animation<>( 3.0f );
		anim.add( 0.0f, new Frame.Vector( 0.0f, 0.0f, 0.0f ) )
			.add( 1.0f, new Frame.Vector( 0.0f, 30.0f, 0.0f ) )
			.add( 2.0f, new Frame.Vector( 90.0f, 90.0f, 0.0f ) )
			.add( 3.0f, new Frame.Vector( 180.0f, 180.0f, 180.0f ) );

		assertThat( helper, anim.apply( 0.0f, 0.0f ), new Vector3f( 0.0f, 0.0f, 0.0f ) );
		assertThat( helper, anim.apply( 0.4f, 0.0f ), new Vector3f( 0.0f, 0.4f * 30.0f, 0.0f ) );
		assertThat( helper, anim.apply( 1.0f, 0.0f ), new Vector3f( 0.0f, 30.0f, 0.0f ) );
		assertThat( helper, anim.apply( 1.5f, 0.0f ), new Vector3f( 0.5f * 90.0f, 30.0f + 0.5f * 60.0f, 0.0f ) );
		assertThat( helper, anim.apply( 2.25f, 0.0f ), new Vector3f( 90.0f + 0.25f * 90.0f, 90.0f + 0.25f * 90.0f, 0.25f * 180.0f ) );
		assertThat( helper, anim.apply( 3.0f, 0.1f ), new Vector3f( 180.0f, 180.0f, 180.0f ) );

		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void interpolate( GameTestHelper helper ) {
		assertThat( helper, 0.0f, 0.0f, InterpolationType.LINEAR );
		assertThat( helper, 0.25f, 0.25f, InterpolationType.LINEAR );
		assertThat( helper, 0.5f, 0.5f, InterpolationType.LINEAR );
		assertThat( helper, 0.75f, 0.75f, InterpolationType.LINEAR );
		assertThat( helper, 1.0f, 1.0f, InterpolationType.LINEAR );

		assertThat( helper, 0.0f, 0.0f, InterpolationType.SQUARE );
		assertThat( helper, 0.25f, 0.0625f, InterpolationType.SQUARE );
		assertThat( helper, 0.5f, 0.25f, InterpolationType.SQUARE );
		assertThat( helper, 0.75f, 0.5625f, InterpolationType.SQUARE );
		assertThat( helper, 1.0f, 1.0f, InterpolationType.SQUARE );

		assertThat( helper, 0.0f, 0.0f, InterpolationType.SQUARE_ROOT );
		assertThat( helper, 0.25f, 0.5f, InterpolationType.SQUARE_ROOT );
		assertThat( helper, 0.5f, 0.707f, InterpolationType.SQUARE_ROOT );
		assertThat( helper, 0.75f, 0.866f, InterpolationType.SQUARE_ROOT );
		assertThat( helper, 1.0f, 1.0f, InterpolationType.SQUARE_ROOT );

		assertThat( helper, 0.0f, 0.0f, InterpolationType.CUBE );
		assertThat( helper, 0.25f, 0.0156f, InterpolationType.CUBE );
		assertThat( helper, 0.5f, 0.125f, InterpolationType.CUBE );
		assertThat( helper, 0.75f, 0.4218f, InterpolationType.CUBE );
		assertThat( helper, 1.0f, 1.0f, InterpolationType.CUBE );

		assertThat( helper, 0.0f, 0.0f, InterpolationType.CUBE_ROOT );
		assertThat( helper, 0.25f, 0.6299f, InterpolationType.CUBE_ROOT );
		assertThat( helper, 0.5f, 0.7937f, InterpolationType.CUBE_ROOT );
		assertThat( helper, 0.75f, 0.9085f, InterpolationType.CUBE_ROOT );
		assertThat( helper, 1.0f, 1.0f, InterpolationType.CUBE_ROOT );

		assertThat( helper, 0.0f, 0.0f, InterpolationType.SMOOTH );
		assertThat( helper, 0.25f, 0.15625f, InterpolationType.SMOOTH );
		assertThat( helper, 0.5f, 0.5f, InterpolationType.SMOOTH );
		assertThat( helper, 0.75f, 0.8437f, InterpolationType.SMOOTH );
		assertThat( helper, 1.0f, 1.0f, InterpolationType.SMOOTH );

		helper.succeed();
	}

	private static void assertThat( GameTestHelper helper, float result, float expected ) {
		if( Math.abs( expected - result ) > 1e-3 ) {
			helper.fail( "Animation< Float > returns invalid value (result: %s, expected: %s)".formatted( result, expected ) );
		}
	}

	private static void assertThat( GameTestHelper helper, Vector3f result, Vector3f expected ) {
		if( !( Math.abs( result.x() - expected.x() ) + Math.abs( result.y() - expected.y() ) + Math.abs( result.z() - expected.z() ) < 1e-3f ) ) {
			helper.fail( "Animation< Vector > returns invalid value (result: %s, expected: %s)".formatted( result, expected ) );
		}
	}

	private static void assertThat( GameTestHelper helper, float value, float expected, InterpolationType type ) {
		float result = type.apply( value );
		if( Math.abs( expected - result ) > 1e-3 ) {
			helper.fail( "InterpolationType %s returns invalid value (result: %s, expected: %s)".formatted( type.toString(), result, expected ) );
		}
	}

	public AnimationTests() {
		super( AnimationTests.class );
	}
}
