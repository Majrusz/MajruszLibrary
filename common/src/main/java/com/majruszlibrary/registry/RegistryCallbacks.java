package com.majruszlibrary.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RegistryCallbacks {
	private Map< Class< ? >, List< Consumer< ? > > > callbacks = new HashMap<>();

	public < Type > void add( Class< Type > clazz, Consumer< Type > consumer ) {
		this.callbacks.computeIfAbsent( clazz, subclazz->new ArrayList<>() ).add( consumer );
	}

	public < Type > void execute( Class< Type > clazz, Type object ) {
		this.get( clazz ).forEach( consumer -> consumer.accept( object ) );
	}

	public < Type > List< Consumer< Type > > get( Class< Type > clazz ) {
		if( this.callbacks.containsKey( clazz ) ) {
			return this.callbacks.get( clazz ).stream().map( consumer->( Consumer< Type > )consumer ).toList();
		} else {
			return List.of();
		}
	}
}
