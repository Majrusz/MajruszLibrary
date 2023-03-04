package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.math.AnyPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import org.joml.Vector3f;

import java.util.function.Supplier;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class AnyPosTests extends BaseTest {
	static final double ERROR = 1e-7;

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void canonicity( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos is not canonical (values passed to AnyPos are different from those returned)";

		assertThat( helper, AnyPos.from( new Vec3( 0.0, 0.1, -0.9 ) ), new Vec3( 0.0, 0.1, -0.9 ), testMessage );
		assertThat( helper, AnyPos.from( new Vector3f( -0.1f, 0.2f, -0.3f ) ), new Vector3f( -0.1f, 0.2f, -0.3f ), testMessage );
		assertThat( helper, AnyPos.from( new Vec3i( 0, 3, 4 ) ), new Vec3i( 0, 3, 4 ), testMessage );
		assertThat( helper, AnyPos.from( new BlockPos( -1, 4, 23 ) ), new BlockPos( -1, 4, 23 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0, 0.4f, 3 ), new Vec3( 1.0, 0.4f, 3 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void add( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not add objects properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).add( new Vec3( 0.2, 0.1, 0.0 ) ), new Vec3( 0.2, 0.2, 0.2 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).add( new Vector3f( -1.0f, 0.5f, 1.0f ) ), new Vector3f( 0.0f, 0.0f, 0.0f ), testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).add( new Vec3i( 1, 2, 3 ) ), new Vec3i( 1, 3, 3 ), testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).add( new BlockPos( -1, 0, 1 ) ), new BlockPos( -2, 0, 2 ), testMessage );
		assertThat( helper, AnyPos.from( 0, 0, 0 ).add( 1 ), new Vec3( 1, 1, 1 ), testMessage );
		assertThat( helper, AnyPos.from( 0, 0, 0 ).add( 1, 2, 3 ), new Vec3( 1, 2, 3 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void sub( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not subtract objects properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).sub( new Vec3( 0.2, 0.1, 0.0 ) ), new Vec3( -0.2, 0.0, 0.2 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).sub( new Vector3f( -1.0f, 0.5f, 1.0f ) ), new Vector3f( 2.0f, -1.0f, -2.0f ), testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).sub( new Vec3i( 1, 2, 3 ) ), new Vec3i( -1, -1, -3 ), testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).sub( new BlockPos( -1, 0, 1 ) ), new BlockPos( 0, 0, 0 ), testMessage );
		assertThat( helper, AnyPos.from( 0, 0, 0 ).sub( 1 ), new Vec3( -1, -1, -1 ), testMessage );
		assertThat( helper, AnyPos.from( 0, 0, 0 ).sub( 1, 2, 3 ), new Vec3( -1, -2, -3 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void mul( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not multiply objects properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).mul( new Vec3( 0.2, 0.1, 0.0 ) ), new Vec3( 0.0, 0.01, 0.0 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).mul( new Vector3f( -1.0f, 0.5f, 1.0f ) ), new Vector3f( -1.0f, -0.25f, -1.0f ), testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).mul( new Vec3i( 1, 2, 3 ) ), new Vec3i( 0, 2, 0 ), testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).mul( new BlockPos( -1, 0, 1 ) ), new BlockPos( 1, 0, 1 ), testMessage );
		assertThat( helper, AnyPos.from( 0, 2, 0 ).mul( 1 ), new Vec3( 0, 2, 0 ), testMessage );
		assertThat( helper, AnyPos.from( 1, 1, 1 ).mul( 1, 2, 3 ), new Vec3( 1, 2, 3 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void div( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not divide objects properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).div( new Vec3( 0.2, 0.1, -0.1 ) ), new Vec3( 0.0, 1.0, -2.0 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).div( new Vector3f( -1.0f, 0.5f, 1.0f ) ), new Vector3f( -1.0f, -1.0f, -1.0f ), testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).div( new Vec3i( 1, 2, 3 ) ), new Vec3i( 0, 0, 0 ), testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).div( new BlockPos( -1, 1, 1 ) ), new BlockPos( 1, 0, 1 ), testMessage );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).div( 1 ), new Vec3( 1, 2, 3 ), testMessage );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).div( 1, 2, 3 ), new Vec3( 1, 1, 1 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void dot( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not calculate scalar product properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).dot( new Vec3( 0.2, 0.1, -0.1 ) ), 0.0 + 0.01 - 0.02, testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).dot( new Vector3f( -1.0f, 0.5f, 1.0f ) ), -1.0f - 0.25f - 1.0f, testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).dot( new Vec3i( 1, 2, 3 ) ), 0 + 2 + 0, testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).dot( new BlockPos( -1, 1, 1 ) ), 1 + 0 + 1, testMessage );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).dot( 1, 2, 3 ), 1 + 4 + 9, testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void dist( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not calculate distance properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).dist( new Vec3( 0.2, 0.1, -0.1 ) ), Math.sqrt( 0.04 + 0.0 + 0.09 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).dist( new Vector3f( -1.0f, 0.5f, 1.0f ) ), Math.sqrt( 4.0f + 1.0f + 4.0f ), testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).dist( new Vec3i( 1, 2, 3 ) ), Math.sqrt( 1 + 1 + 9 ), testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).dist( new BlockPos( -1, 1, 1 ) ), Math.sqrt( 0 + 1 + 0 ), testMessage );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).dist( 1, 2, 3 ), Math.sqrt( 0 + 0 + 0 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void distSqr( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not calculate squared distance properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).distSqr( new Vec3( 0.2, 0.1, -0.1 ) ), 0.04 + 0.0 + 0.09, testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).distSqr( new Vector3f( -1.0f, 0.5f, 1.0f ) ), 4.0f + 1.0f + 4.0f, testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).distSqr( new Vec3i( 1, 2, 3 ) ), 1 + 1 + 9, testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).distSqr( new BlockPos( -1, 1, 1 ) ), 0 + 1 + 0, testMessage );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).distSqr( 1, 2, 3 ), 0 + 0 + 0, testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void dist2d( GameTestHelper helper ) {
		Supplier< String > testMessage = ()->"AnyPos does not calculate distance properly";

		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).dist2d( new Vec3( 0.2, 0.1, -0.1 ) ), Math.sqrt( 0.04 + 0.09 ), testMessage );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).dist2d( new Vector3f( -1.0f, 0.5f, 1.0f ) ), Math.sqrt( 4.0f + 4.0f ), testMessage );
		assertThat( helper, AnyPos.from( 0, 1, 0 ).dist2d( new Vec3i( 1, 2, 3 ) ), Math.sqrt( 1 + 9 ), testMessage );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).dist2d( new BlockPos( -1, 1, 1 ) ), Math.sqrt( 0 + 0 ), testMessage );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).dist2d( 1, 2, 3 ), Math.sqrt( 0 + 0 ), testMessage );

		helper.succeed();
	}

	@GameTest( templateNamespace = "mlib", template = "empty_test" )
	public static void utility( GameTestHelper helper ) {
		assertThat( helper, AnyPos.from( 0.0, 0.1, 0.2 ).len(), Math.sqrt( 0.01 + 0.04 ), ()->"AnyPos does not calculate length properly" );
		assertThat( helper, AnyPos.from( 1.0f, -0.5f, -1.0f ).norm(), new Vector3f( 2.0f / 3.0f, -1.0f / 3.0f, -2.0f / 3.0f ), ()->"AnyPos does not calculate normalized vector properly" );
		assertThat( helper, AnyPos.from( 1, 2, 3 ).norm().len(), 1, ()->"AnyPos does not calculate normalized vector properly" );
		assertThat( helper, AnyPos.from( -1, 0, 1 ).center(), new Vec3( -0.5f, 0.5f, 1.5f ), ()->"AnyPos does not centered vector properly" );

		helper.succeed();
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, Vec3 expected, Supplier< String > message ) {
		Vec3 vec3 = result.vec3();
		boolean condition = Math.abs( vec3.x - expected.x ) < ERROR && Math.abs( vec3.y - expected.y ) < ERROR && Math.abs( vec3.z - expected.z ) < ERROR;

		assertThat( helper, condition, message );
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, Vector3f expected, Supplier< String > message ) {
		Vector3f vec3f = result.vec3f();
		boolean condition = Math.abs( vec3f.x - expected.x ) < ERROR && Math.abs( vec3f.y - expected.y ) < ERROR && Math.abs( vec3f.z - expected.z ) < ERROR;

		assertThat( helper, condition, message );
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, Vec3i expected, Supplier< String > message ) {
		Vec3i vec3i = result.vec3i();
		boolean condition = Math.abs( vec3i.getX() - expected.getX() ) < ERROR && Math.abs( vec3i.getY() - expected.getY() ) < ERROR && Math.abs( vec3i.getY() - expected.getY() ) < ERROR;

		assertThat( helper, condition, message );
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, BlockPos expected, Supplier< String > message ) {
		BlockPos blockPos = result.block();
		boolean condition = Math.abs( blockPos.getX() - expected.getX() ) < ERROR && Math.abs( blockPos.getY() - expected.getY() ) < ERROR && Math.abs( blockPos.getY() - expected.getY() ) < ERROR;

		assertThat( helper, condition, message );
	}

	public static void assertThat( GameTestHelper helper, Number result, Number expected, Supplier< String > message ) {
		assertThat( helper, Math.abs( result.doubleValue() - expected.doubleValue() ) < ERROR, message );
	}

	public AnyPosTests() {
		super( AnyPosTests.class );
	}
}
