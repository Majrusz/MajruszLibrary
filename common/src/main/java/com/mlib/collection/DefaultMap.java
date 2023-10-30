package com.mlib.collection;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DefaultMap< KeyType, ValueType > implements Map< KeyType, ValueType > {
	private final ValueType defaultValue;
	private final Object2ObjectMap< KeyType, ValueType > map;

	public static < KeyType, ValueType > DefaultMap< KeyType, ValueType > of( Map< KeyType, ValueType > map ) {
		return new DefaultMap<>( new Object2ObjectOpenHashMap<>( map ) );
	}

	@SafeVarargs
	public static < KeyType, ValueType > DefaultMap< KeyType, ValueType > of( Entry< KeyType, ValueType >... entries ) {
		Object2ObjectOpenHashMap< KeyType, ValueType > map = new Object2ObjectOpenHashMap<>();
		Arrays.stream( entries ).forEach( entry->map.put( entry.key, entry.value ) );

		return new DefaultMap<>( map );
	}

	public static < KeyType, ValueType > Entry< KeyType, ValueType > entry( KeyType key, ValueType value ) {
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
	public ValueType get( Object key ) {
		ValueType value = this.map.get( key );

		return value != null ? value : this.defaultValue;
	}

	@Nullable
	@Override
	public ValueType put( KeyType key, ValueType value ) {
		return this.map.put( key, value );
	}

	@Override
	public ValueType remove( Object key ) {
		return this.map.remove( key );
	}

	@Override
	public void putAll( @NotNull Map< ? extends KeyType, ? extends ValueType > map ) {
		this.map.forEach( this::put );
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@NotNull
	@Override
	public Set< KeyType > keySet() {
		return this.map.keySet();
	}

	@NotNull
	@Override
	public Collection< ValueType > values() {
		return this.map.values();
	}

	@NotNull
	@Override
	public Set< java.util.Map.Entry< KeyType, ValueType > > entrySet() {
		return this.map.entrySet();
	}

	private DefaultMap( Object2ObjectOpenHashMap< KeyType, ValueType > map ) {
		this.defaultValue = map.get( "default" );
		this.map = map;

		if( this.defaultValue == null ) {
			throw new IllegalArgumentException( "There is no key that stores the default value" );
		}
	}

	public record Entry< Key, Value >( Key key, Value value ) {}
}
