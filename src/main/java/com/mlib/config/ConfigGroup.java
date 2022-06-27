package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a group that contains many configs (and other groups). */
public class ConfigGroup extends UserConfig {
	protected List< UserConfig > configs = new ArrayList<>();

	public ConfigGroup( String groupName, String comment, UserConfig... configs ) {
		super( groupName, comment );
		this.addConfigs( configs );
	}

	public ConfigGroup( UserConfig... configs ) {
		this( "", "", configs );
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		if( this.name.isEmpty() ) {
			this.configs.forEach( config->config.build( builder ) );
		} else {
			if( !this.comment.isEmpty() ) {
				builder.comment( this.comment );
			}

			builder.push( this.name );
			this.configs.forEach( config->config.build( builder ) );
			builder.pop();
		}
	}

	public ConfigGroup addGroup( ConfigGroup group ) {
		return addConfig( group );
	}

	public void addGroups( ConfigGroup... groups ) {
		addConfigs( groups );
	}

	public ConfigGroup addNewGroup( String groupName, String comment, UserConfig... configs ) {
		return addGroup( new ConfigGroup( groupName, comment, configs ) );
	}

	public < ConfigType extends UserConfig > ConfigType addConfig( ConfigType config ) {
		this.configs.add( config );

		return config;
	}

	public void addConfigs( UserConfig... configs ) {
		this.configs.addAll( Arrays.asList( configs ) );
	}
}
