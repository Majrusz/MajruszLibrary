package com.majruszlibrary.modhelper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.Reader;
import java.util.function.Supplier;

public class Resource< Type > implements Supplier< Type > {
	final Class< Type > clazz;
	Type value = null;

	public Resource( Class< Type > clazz ) {
		this.clazz = clazz;
	}

	@Override
	public Type get() {
		return this.value;
	}

	void load( Gson gson, JsonElement json ) {
		this.value = gson.fromJson( json, this.clazz );
	}

	void load( Gson gson, Reader reader ) {
		this.value = gson.fromJson( reader, this.clazz );
	}

	static class Lazy< Type > extends Resource< Type > {
		private final Runnable loadCallback;

		public Lazy( Class< Type > clazz, Runnable loadCallback ) {
			super( clazz );

			this.loadCallback = loadCallback;
		}

		@Override
		public Type get() {
			if( super.get() == null ) {
				this.loadCallback.run();
			}

			return super.get();
		}
	}
}
