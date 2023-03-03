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
		Vec3 vec3 = new Vec3( 0.0, 0.1, -0.9 );
		Vector3f vec3f = new Vector3f( -0.1f, 0.2f, -0.3f );
		Vec3i vec3i = new Vec3i( 0, 3, 4 );
		BlockPos blockPos = new BlockPos( -1, 4, 23 );
		double x = 1.0;
		float y = 0.4f;
		int z = 3;

		assertThat( helper, AnyPos.from( vec3 ).vec3(), vec3, testMessage );
		assertThat( helper, AnyPos.from( vec3f ).vec3f(), vec3f, testMessage );
		assertThat( helper, AnyPos.from( vec3i ).vec3i(), vec3i, testMessage );
		assertThat( helper, AnyPos.from( blockPos ).block(), blockPos, testMessage );
		assertThat( helper, AnyPos.from( x, y, z ).vec3(), new Vec3( x, y, z ), testMessage );

		helper.succeed();
	}

	public AnyPosTests() {
		super( AnyPosTests.class );
	}
}
