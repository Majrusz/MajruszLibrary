package com.majruszlibrary.collection;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DefaultMap< Type > implements Map< String, Type > {
	private static final String DEFAULT_ID = "__default";
	private final Type defaultValue;
	private final Object2ObjectMap< String, Type > map;

	public static < Type > DefaultMap< Type > of( Map< String, Type > map ) {
		return new DefaultMap<>( new Object2ObjectOpenHashMap<>( map ) );
	}

	@SafeVarargs
	public static < Type > DefaultMap< Type > of( Entry< String, Type >... entries ) {
		Object2ObjectOpenHashMap< String, Type > map = new Object2ObjectOpenHashMap<>();
		Arrays.stream( entries ).forEach( entry->map.put( entry.key, entry.value ) );

		return new DefaultMap<>( map );
	}

	public static < Type > Entry< String, Type > defaultEntry( Type value ) {
		return new Entry<>( DEFAULT_ID, value );
	}

	public static < Type > Entry< String, Type > entry( String key, Type value ) {
		return new Entry<>( key, value );
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey( Object key ) {
		return this.map.containsKey( key );
	}

	@Override
	public boolean containsValue( Object value ) {
		return this.map.containsValue( value );
	}

	@Override
	public Type get( Object key ) {
		Type value = this.map.get( key );

		return value != null ? value : this.defaultValue;
	}

	@Nullable
	@Override
	public Type put( String key, Type value ) {
		return this.map.put( key, value );
	}

	@Override
	public Type remove( Object key ) {
		return this.map.remove( key );
	}

	@Override
	public void putAll( @NotNull Map< ? extends String, ? extends Type > map ) {
		this.map.forEach( this::put );
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@NotNull
	@Override
	public Set< String > keySet() {
		return this.map.keySet();
	}

	@NotNull
	@Override
	public Collection< Type > values() {
		return this.map.values();
	}

	@NotNull
	@Override
	public Set< java.util.Map.Entry< String, Type > > entrySet() {
		return this.map.entrySet();
	}

	private DefaultMap( Object2ObjectOpenHashMap< String, Type > map ) {
		this.defaultValue = map.get( DEFAULT_ID );
		this.map = map;

		if( this.defaultValue == null ) {
			throw new IllegalArgumentException( "There is no key that stores the default value" );
		}
	}

	public record Entry< Key, Value >( Key key, Value value ) {}
}
