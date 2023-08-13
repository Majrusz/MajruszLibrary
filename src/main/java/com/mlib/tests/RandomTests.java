package com.mlib.tests;

import com.mlib.MajruszLibrary;
import com.mlib.Random;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GameTestHolder( MajruszLibrary.MOD_ID )
public class RandomTests extends BaseTest {
	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void list( GameTestHelper helper ) {
		List< String > list1 = List.of( "a", "b", "c" );
		for( int i = 0; i < list1.size() * 2; ++i ) {
			assertThat( helper, list1, Random.next( list1 ) );
		}

		List< String > list2 = List.of();
		assertThat( helper, list2, Random.next( list2 ) );
		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void set( GameTestHelper helper ) {
		Set< String > set1 = Set.of( "a", "b", "c" );
		for( int i = 0; i < set1.size() * 2; ++i ) {
			assertThat( helper, set1, Random.next( set1 ) );
		}

		List< String > set2 = List.of();
		assertThat( helper, set2, Random.next( set2 ) );
		helper.succeed();
	}

	@GameTest( templateNamespace = MajruszLibrary.MOD_ID, template = "empty" )
	public static void map( GameTestHelper helper ) {
		Map< String, String > map1 = Map.of( "1", "a", "2", "b", "3", "c" );
		for( int i = 0; i < map1.size() * 2; ++i ) {
			assertThat( helper, map1, Random.next( map1 ) );
		}

		List< String > map2 = List.of();
		assertThat( helper, map2, Random.next( map2 ) );
		helper.succeed();
	}

	private static < Type > void assertThat( GameTestHelper helper, Collection< Type > collection, Type element ) {
		if( element != null ) {
			assertThat( helper, collection.contains( element ), ()->"Random.next() returns invalid element" );
		} else {
			assertThat( helper, collection.isEmpty(), ()->"Random.next() returns invalid element" );
		}
	}

	private static < Type1, Type2 > void assertThat( GameTestHelper helper, Map< Type1, Type2 > map, Map.Entry< Type1, Type2 > element ) {
		if( element != null ) {
			assertThat( helper, map.containsKey( element.getKey() ), ()->"Random.next() returns invalid element" );
		} else {
			assertThat( helper, map.isEmpty(), ()->"Random.next() returns invalid element" );
		}
	}

	public RandomTests() {
		super( RandomTests.class );
	}
}
