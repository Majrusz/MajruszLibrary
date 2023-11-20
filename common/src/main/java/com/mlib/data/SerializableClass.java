package com.mlib.data;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SerializableClass< Type > extends SerializableObject< Type > {
	public < ValueType > SerializableClass< Type > define( String id, IReader< ValueType > reader, Supplier< ValueType > getter,
		Consumer< ValueType > setter
	) {
		this.readers.add( new ReaderKey<>( id, reader, s->getter.get(), ( s, v )->setter.accept( v ) ) );

		return this;
	}

	public SerializableClass< Type > define( String id, Class< ? > clazz ) {
		this.readers.add( new ReaderKey<>( id, new ReaderClass( clazz ), s->null, ( s, v )->{} ) );

		return this;
	}
}
