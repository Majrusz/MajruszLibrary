package com.mlib.gamemodifiers;

import com.mlib.MajruszLibrary;
import com.mlib.config.ConfigGroup;
import com.mlib.config.ConfigHandler;

import java.util.HashMap;

import static com.mlib.MajruszLibrary.MOD_CONFIGS;

public class ModConfigs {
	final HashMap< String, ConfigGroup > map = new HashMap<>();

	public static ConfigGroup init( ConfigHandler handler, String id ) {
		ConfigGroup group = MOD_CONFIGS.init( id );
		handler.addConfig( group );

		return group;
	}

	public static ConfigGroup init( String parentId, String id ) {
		ConfigGroup group = MOD_CONFIGS.init( id );
		MOD_CONFIGS.get( parentId ).addConfig( group );

		return group;
	}

	public static ConfigGroup registerSubgroup( String parentId ) {
		return MOD_CONFIGS.register( parentId );
	}

	private synchronized ConfigGroup init( String id ) {
		assert !this.map.containsKey( id ) : String.format( "Group '%s' has been initialized already!", id );
		ConfigGroup group = new ConfigGroup();
		this.map.put( id, group );

		MajruszLibrary.logOnDev( "[ModConfigs] Group '%s' has been initialized.", id );

		return group;
	}

	private synchronized ConfigGroup register( String id ) {
		assert this.map.containsKey( id ) : String.format( "Group '%s' has not been initialized yet!", id );
		ConfigGroup group = new ConfigGroup();
		this.map.get( id ).addConfig( group );

		MajruszLibrary.logOnDev( "[ModConfigs] Subgroup has been added to '%s'.", id );

		return group;
	}

	private synchronized ConfigGroup get( String id ) {
		assert this.map.containsKey( id ) : String.format( "Group '%s' has not been initialized yet!", id );

		return this.map.get( id );
	}
}
