package com.mlib.gamemodifiers;

import com.mlib.MajruszLibrary;
import com.mlib.config.ConfigGroup;

import java.util.HashMap;

public class ModConfigs {
	final HashMap< String, ConfigGroup > map = new HashMap<>();

	public synchronized void setup( String key, ConfigGroup group ) {
		assert !this.map.containsKey( key ) : "Config for " + key + " has been initialized already!";
		this.map.put( key, group );

		MajruszLibrary.logOnDev( "[ModConfigs] Game modifier group '%s' has been initialized.", key );
	}

	public synchronized void insert( String key, GameModifier modifier ) {
		assert this.map.containsKey( key ) : "Config for " + key + " has not been initialized yet!";
		this.map.get( key ).addConfig( modifier );

		String name = modifier.getName();
		String message = name.isEmpty() ? String.format( "Unnamed game modifier '%s'", modifier ) : String.format( "Game modifier '%s'", name );
		MajruszLibrary.logOnDev( "[ModConfigs] %s has been inserted to '%s'.", message, key );
	}

	public synchronized void insert( GameModifier modifier ) {
		this.insert( GameModifier.DEFAULT_KEY, modifier );
	}

	public synchronized boolean has( String key ) {
		return this.map.containsKey( key );
	}

	public synchronized ConfigGroup get( String key ) {
		assert this.map.containsKey( key ) : "Config for " + key + " has not been initialized yet!";

		return this.map.get( key );
	}
}
