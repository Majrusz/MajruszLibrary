package com.majruszlibrary.collection;

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

	public static < K, V1, V2, M extends Map< K, V2 > > M map( Map< K, V1 > input, Function< V1, V2 > mapper, Supplier< M > instance ) {
		M output = instance.get();
		for( K key : input.keySet() ) {
			output.put( key, mapper.apply( input.get( key ) ) );
		}

		return output;
	}

	public static < K1, K2, V, M extends Map< K2, V > > M mapKey( Map< K1, V > input, Function< K1, K2 > mapper, Supplier< M > instance ) {
		M output = instance.get();
		for( K1 key : input.keySet() ) {
			output.put( mapper.apply( key ), input.get( key ) );
		}

		return output;
	}
}
