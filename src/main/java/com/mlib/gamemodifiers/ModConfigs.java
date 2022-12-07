package com.mlib.gamemodifiers;

import com.mlib.MajruszLibrary;
import com.mlib.config.ConfigGroup;

import java.util.HashMap;

public class ModConfigs {
	private final HashMap< String, ConfigGroup > map = new HashMap<>();

	public synchronized void setup( String key, ConfigGroup group ) {
		assert !this.map.containsKey( key ) : "Config for " + key + " has been initialized already!";
		this.map.put( key, group );

		MajruszLibrary.log( "Game modifier group '%s' has been initialized.", key );
	}

	public synchronized void insert( String key, GameModifier modifier ) {
		assert this.map.containsKey( key ) : "Config for " + key + " has not been initialized yet!";
		this.map.get( key ).addConfig( modifier );

		MajruszLibrary.log( "Game modifier '%s' has been inserted to '%s'.", modifier.getName(), key );
	}

	public synchronized boolean has( String key ) {
		return this.map.containsKey( key );
	}

	public synchronized ConfigGroup get( String key ) {
		assert this.map.containsKey( key ) : "Config for " + key + " has not been initialized yet!";

		return this.map.get( key );
	}
}
