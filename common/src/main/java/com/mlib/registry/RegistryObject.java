package com.mlib.registry;

import java.util.function.Supplier;

public class RegistryObject< Type > implements Supplier< Type > {
	final RegistryGroup< ? > group;
	final String id;
	final Supplier< Type > newInstance;
	Supplier< Type > value;

	public RegistryObject( RegistryGroup< ? > group, String id, Supplier< Type > newInstance ) {
		this.group = group;
		this.id = id;
		this.newInstance = newInstance;
	}

	public void set( Supplier< Type > value ) {
		this.value = value;
	}

	@Override
	public Type get() {
		return this.value.get();
	}

	public String getId() {
		return this.id;
	}

	void register() {
		Registries.PLATFORM.register( this );
	}
}
