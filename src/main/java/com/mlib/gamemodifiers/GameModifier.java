package com.mlib.gamemodifiers;

import com.mlib.Registries;
import com.mlib.config.ConfigGroup;

import java.util.ArrayList;
import java.util.List;

import static com.mlib.MajruszLibrary.MOD_CONFIGS;

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
	final List< ContextBase< ? extends ContextData > > contexts = new ArrayList<>();
	final String configKey;

	public static ConfigGroup addNewGroup( String key, String name, String comment ) {
		ConfigGroup group = new ConfigGroup( name, comment );
		MOD_CONFIGS.setup( key, group );
		return group;
	}

	public static ConfigGroup addNewGroup( String key ) {
		return addNewGroup( key, "GameModifiers", "" );
	}

	public GameModifier( String configKey, String configName, String configComment ) {
		super( configName, configComment );
		this.configKey = configKey;
		MOD_CONFIGS.insert( configKey, this );
	}

	public GameModifier( String configName, String configComment ) {
		this( DEFAULT_KEY, configName, configComment );
	}

	public < DataType extends ContextData > void addContext( ContextBase< DataType > context ) {
		context.setup( this );
		this.addConfig( context );
	}

	public void addContexts( ContextBase< ? >... contexts ) {
		for( ContextBase< ? > context : contexts ) {
			addContext( context );
		}
	}

	public List< ContextBase< ? extends ContextData > > getContexts() {
		return this.contexts;
	}

	public String getConfigKey() {
		return this.configKey;
	}
}
