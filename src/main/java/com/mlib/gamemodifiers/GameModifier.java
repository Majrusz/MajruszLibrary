package com.mlib.gamemodifiers;

import com.mlib.MajruszLibrary;
import com.mlib.Registries;
import com.mlib.config.ConfigGroup;
import com.mlib.config.ConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 If you would ever wonder what the hell is going over here then let me explain...
 Most of the game changes are "game modifiers", which change or extend certain game
 events with new functionalities via contexts. The context handles specific moment
 in the gameplay (for instance it can be a moment when player is about to take the damage)
 and it is made of conditions, which determines whether we should consider that context.
 It allows for more generic and reusable behaviours for all of my mods and makes porting
 mods much easier.
 */
public abstract class GameModifier extends ConfigGroup {
	public static final String DEFAULT_KEY = Registries.getLocationString( "default" );
	static final HashMap< String, ConfigGroup > MOD_CONFIGS = new HashMap<>();
	static final HashMap< String, List< GameModifier > > PENDING_MODIFIERS = new HashMap<>();
	final List< Context< ? extends ContextData > > contexts = new ArrayList<>();
	final String configKey;

	public static ConfigGroup addNewGroup( String key, String name, String comment ) {
		assert !MOD_CONFIGS.containsKey( key ) : "Config for " + key + " has been initialized already!";

		ConfigGroup group = new ConfigGroup( name, comment );
		MOD_CONFIGS.put( key, group );
		List< GameModifier > pendingModifiers = PENDING_MODIFIERS.remove( key );
		if( pendingModifiers != null ) {
			pendingModifiers.forEach( group::addConfig );
		}
		MajruszLibrary.LOGGER.info( String.format( "Game modifier config '%s' has been initialized. (pending modifiers: %d)", key, pendingModifiers != null ? pendingModifiers.size() : 0 ) );
		return group;
	}

	public static ConfigGroup addNewGroup( String key ) {
		return addNewGroup( key, "GameModifiers", "" );
	}

	public GameModifier( String configKey, String configName, String configComment ) {
		super( configName, configComment );
		this.configKey = configKey;
		this.addToModConfig();
	}

	public GameModifier( String configKey ) {
		this( configKey, "", "" );
	}

	public GameModifier() {
		this( DEFAULT_KEY );
	}

	public < DataType extends ContextData > void addContext( Context< DataType > context ) {
		context.setup( this );
		this.addConfig( context );
	}

	public void addContexts( Context< ? >... contexts ) {
		for( Context< ? > context : contexts ) {
			addContext( context );
		}
	}

	public List< Context< ? extends ContextData > > getContexts() {
		return this.contexts;
	}

	public String getConfigKey() {
		return this.configKey;
	}

	private void addToModConfig() {
		ConfigGroup modConfig = MOD_CONFIGS.get( this.configKey );
		if( modConfig != null ) {
			modConfig.addConfig( this );
		} else { // to prevent race conditions
			List< GameModifier > gameModifiers = PENDING_MODIFIERS.getOrDefault( this.configKey, new ArrayList<>() );
			gameModifiers.add( this );
		}
	}
}
