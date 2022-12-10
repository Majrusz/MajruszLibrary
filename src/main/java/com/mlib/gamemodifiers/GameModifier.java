package com.mlib.gamemodifiers;

import com.mlib.Registries;
import com.mlib.config.ConfigGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

	public static ConfigGroup addNewGroup( ConfigGroup parent, String key, String name, String comment ) {
		ConfigGroup group = addNewGroup( key, name, comment );
		parent.addGroup( group );

		return group;
	}

	public static ConfigGroup addNewGroup( ConfigGroup parent, String key ) {
		return addNewGroup( parent, key, "GameModifiers", "" );
	}

	public static ConfigGroup addNewGroup( String parentKey, String key, String name, String comment ) {
		return addNewGroup( MOD_CONFIGS.get( parentKey ), key, name, comment );
	}

	public GameModifier( String configKey, String name, String comment ) {
		super( name, comment );
		this.configKey = configKey;
		MOD_CONFIGS.insert( configKey, this );
	}

	public GameModifier( String name, String comment ) {
		this( DEFAULT_KEY, name, comment );
	}

	public GameModifier() {
		this( DEFAULT_KEY, "", "" );
	}

	public < DataType extends ContextData > void addContext( ContextBase< DataType > context ) {
		context.setup( this );
		this.addConfig( context );
	}

	public void addContexts( ContextBase< ? >... contexts ) {
		Stream.of( contexts ).forEach( this::addContext );
	}

	public List< ContextBase< ? extends ContextData > > getContexts() {
		return this.contexts;
	}

	public String getConfigKey() {
		return this.configKey;
	}
}
