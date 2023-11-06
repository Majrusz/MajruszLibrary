package com.mlib.registry;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistryObject< Type > implements Supplier< Type > {
	final RegistryGroup< ? > group;
	final String id;
	final Supplier< Type > newInstance;
	Supplier< Type > value;
	Supplier< Boolean > isPresent;

	public RegistryObject( RegistryGroup< ? > group, String id, Supplier< Type > newInstance ) {
		this.group = group;
		this.id = id;
		this.newInstance = newInstance;
	}

	@Override
	public Type get() {
		return this.value.get();
	}

	public void set( Supplier< Type > value, Supplier< Boolean > isPresent ) {
		this.value = value;
		this.isPresent = isPresent;
	}

	public void ifPresent( Consumer< Type > consumer ) {
		if( this.isPresent() ) {
			consumer.accept( this.get() );
		}
	}

	public String getId() {
		return this.id;
	}

	public boolean isPresent() {
		return this.isPresent.get();
	}

	void register() {
		Registries.PLATFORM.register( this );
	}
}
