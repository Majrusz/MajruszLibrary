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

	public static void assertThat( GameTestHelper helper, AnyPos result, Vec3 expected, Supplier< String > message ) {
		assertThat( helper, result.vec3(), expected, message );
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, Vector3f expected, Supplier< String > message ) {
		assertThat( helper, result.vec3f(), expected, message );
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, Vec3i expected, Supplier< String > message ) {
		assertThat( helper, result.vec3i(), expected, message );
	}

	public static void assertThat( GameTestHelper helper, AnyPos result, BlockPos expected, Supplier< String > message ) {
		assertThat( helper, result.block(), expected, message );
	}

	public AnyPosTests() {
		super( AnyPosTests.class );
	}
}
