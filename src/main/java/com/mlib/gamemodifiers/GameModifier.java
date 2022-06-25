package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 If you would ever wonder what the hell is going over here then let me explain...
 Most of the game changes are "game modifiers", which change or extend certain game
 events with new functionalities via contexts. The context handles specific moment
 in the gameplay (for instance it can be a moment when player is about to take the damage)
 and it is made of conditions, which determines whether we should consider that context.
 It allows for more generic and reusable behaviours for all of my mods.
 */
public abstract class GameModifier {
	static final HashMap< String, ConfigGroup > MOD_CONFIGS = new HashMap<>();
	protected final List< Config > configs = new ArrayList<>();
	protected final Context[] contexts;
	protected final ConfigGroup configGroup;
	protected final String configKey;

	public static ConfigGroup addNewGroup( String configKey, String configName, String configComment ) {
		assert !MOD_CONFIGS.containsKey( configKey ) : "Config for " + configKey + " has been initialized already!";

		ConfigGroup group = new ConfigGroup( configName, configComment );
		MOD_CONFIGS.put( configKey, group );
		return group;
	}

	public static ConfigGroup addNewGroup( String configKey ) {
		return addNewGroup( configKey, "GameModifiers", "" );
	}

	public GameModifier( String configKey, String configName, String configComment, Context... contexts ) {
		assert MOD_CONFIGS.containsKey( configKey ) : "Config for " + configKey + " has not been initialized yet!";

		this.contexts = contexts;
		this.configGroup = MOD_CONFIGS.get( configKey ).addNewGroup( configName, configComment );
		this.configKey = configKey;

		assert this.contexts.length > 0;
		for( Context context : this.contexts ) {
			context.setup( this );
		}
	}

	public abstract void execute( Object data );

	public void addConfigs( Config... configs ) {
		for( Config config : configs ) {
			config.setup( configs.length > 1 ? config.addNewGroup( this.configGroup ) : this.configGroup );
			this.configs.add( config );
		}
	}

	public Context[] getContexts() {
		return this.contexts;
	}

	public int getConfiguredContextsLength() {
		int sum = 0;
		for( Context context : this.contexts ) {
			if( context.configs.size() > 0 ) {
				++sum;
			}
		}

		return sum;
	}

	public ConfigGroup getConfigGroup() {
		return this.configGroup;
	}

	public String getConfigKey() {
		return this.configKey;
	}
}
