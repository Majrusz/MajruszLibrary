package com.mlib.gamemodifiers;

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
	final List< ContextBase< ? > > contexts = new ArrayList<>();
	final String configId;

	public GameModifier( String configId ) {
		this.configId = configId;

		MOD_CONFIGS.insert( configId, this );
	}

	public GameModifier() {
		this( ModConfigs.DEFAULT_ID );
	}

	public List< ContextBase< ? > > getContexts() {
		return Collections.unmodifiableList( this.contexts );
	}

	public String getConfigId() {
		return this.configId;
	}
}
