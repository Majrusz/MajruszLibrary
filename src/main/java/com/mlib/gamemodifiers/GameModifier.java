package com.mlib.gamemodifiers;

import com.mlib.Registries;
import com.mlib.config.ConfigGroup;

import java.util.ArrayList;
import java.util.Collections;
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
	public static final String DEFAULT_ID = Registries.getLocationString( "default" );
	final List< ContextBase< ? extends ContextData > > contexts = new ArrayList<>();
	final String configKey;

	public GameModifier( String configKey ) {
		this.configKey = configKey;

		MOD_CONFIGS.insert( configKey, this );
	}

	public GameModifier() {
		this( DEFAULT_ID );
	}

	public < DataType extends ContextData > void addContext( ContextBase< DataType > context ) {
		context.setup( this );
		this.addConfig( context );
	}

	public List< ContextBase< ? extends ContextData > > getContexts() {
		return Collections.unmodifiableList( this.contexts );
	}

	public String getConfigKey() {
		return this.configKey;
	}
}
