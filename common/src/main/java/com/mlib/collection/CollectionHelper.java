package com.mlib.collection;

import java.util.Collection;
import java.util.function.Supplier;

public class CollectionHelper {
	public static < ValueType, ReturnType extends Collection< ValueType > > ReturnType pop( Collection< ValueType > values, Supplier< ReturnType > supplier ) {
		ReturnType copy = supplier.get();
		copy.addAll( values );
		values.clear();

		return copy;
	}
}
