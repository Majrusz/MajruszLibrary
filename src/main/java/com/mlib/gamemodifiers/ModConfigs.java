package com.mlib.gamemodifiers;

import com.mlib.MajruszLibrary;
import com.mlib.config.ConfigGroup;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

import static com.mlib.MajruszLibrary.MOD_CONFIGS;

public class ModConfigs {
	public static final String DEFAULT_ID = new ResourceLocation( MajruszLibrary.MOD_ID, "default" ).toString();
	final HashMap< String, ConfigGroup > map = new HashMap<>();

	public static ConfigGroup setup( String id ) {
		ConfigGroup group = new ConfigGroup();
		MOD_CONFIGS.init( id, group );

		return group;
	}

	public static ConfigGroup setup( ConfigGroup parent, String id ) {
		ConfigGroup group = setup( id );
		parent.addConfig( group );

		return group;
	}

	public static ConfigGroup setup( String parentId, String id ) {
		return setup( MOD_CONFIGS.get( parentId ), id );
	}

	public ModConfigs() {
		this.init( ModConfigs.DEFAULT_ID, new ConfigGroup() );
	}

	public synchronized void init( String id, ConfigGroup group ) {
		assert !this.map.containsKey( id ) : "Config for " + id + " has been initialized already!";
		this.map.put( id, group );

		MajruszLibrary.logOnDev( "[ModConfigs] Game modifier group '%s' has been initialized.", id );
	}

	public synchronized void insert( String id, GameModifier modifier ) {
		assert this.map.containsKey( id ) : "Config for " + id + " has not been initialized yet!";
		this.map.get( id ).addConfig( modifier );

		String name = modifier.getName();
		String message = name.isEmpty() ? String.format( "Unnamed game modifier '%s'", modifier ) : String.format( "Game modifier '%s'", name );
		MajruszLibrary.logOnDev( "[ModConfigs] %s has been inserted to '%s'.", message, id );
	}

	public synchronized void insert( GameModifier modifier ) {
		this.insert( DEFAULT_ID, modifier );
	}

	public synchronized boolean has( String id ) {
		return this.map.containsKey( id );
	}

	public synchronized ConfigGroup get( String id ) {
		assert this.map.containsKey( id ) : "Config for " + id + " has not been initialized yet!";

		return this.map.get( id );
	}
}
