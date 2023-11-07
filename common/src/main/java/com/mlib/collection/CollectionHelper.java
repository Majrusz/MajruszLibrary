package com.mlib.collection;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionHelper {
	public static < ValueType, ReturnType extends Collection< ValueType > > ReturnType pop( Collection< ValueType > values, Supplier< ReturnType > supplier ) {
		ReturnType copy = supplier.get();
		copy.addAll( values );
		values.clear();

		return copy;
	}

	public static < K, V1, V2 > Map< K, V2 > map( Map< K, V1 > input, Function< V1, V2 > mapper, Supplier< Map< K, V2 > > instance ) {
		Map< K, V2 > output = instance.get();
		for( K key : input.keySet() ) {
			output.put( key, mapper.apply( input.get( key ) ) );
		}

		return output;
	}
}
