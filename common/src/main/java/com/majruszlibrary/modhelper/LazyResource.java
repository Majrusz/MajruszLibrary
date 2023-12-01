package com.majruszlibrary.modhelper;

import java.util.function.Supplier;

public class LazyResource< Type > implements Supplier< Type > {
	private final Supplier< Type > supplier;
	public Type value;

	public static < Type > LazyResource< Type > of( Supplier< Type > supplier ) {
		return new LazyResource<>( supplier );
	}

	@Override
	public Type get() {
		if( this.value == null ) {
			this.value = this.supplier.get();
		}

		return this.value;
	}

	private LazyResource( Supplier< Type > supplier ) {
		this.supplier = supplier;
	}
}
