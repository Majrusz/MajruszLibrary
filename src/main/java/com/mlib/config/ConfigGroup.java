package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Class representing group of base config values. */
public class ConfigGroup implements IConfig {
	protected List< IConfig > configTypeList = new ArrayList<>();
	private final String groupName;
	private final String comment;

	public ConfigGroup( String groupName, String comment ) {
		this.groupName = groupName;
		this.comment = comment;
	}

	public ConfigGroup( String groupName, String comment, IConfig... configs ) {
		this( groupName, comment );
		this.addConfigs( configs );
	}

	/** Builds all configs from the list. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		if( !this.comment.equals( "" ) )
			builder.comment( this.comment );

		builder.push( this.groupName );
		for( IConfig config : this.configTypeList )
			config.build( builder );
		builder.pop();
	}

	/** Adds new group to the list. */
	public ConfigGroup addGroup( ConfigGroup group ) {
		this.configTypeList.add( group );

		return group;
	}

	/** Adds new groups to the list. */
	public void addGroups( ConfigGroup... groups ) {
		this.configTypeList.addAll( Arrays.asList( groups ) );
	}

	/** Adds new config to the list. */
	public < ConfigType extends IConfig > ConfigType addConfig( ConfigType config ) {
		this.configTypeList.add( config );

		return config;
	}

	/** Adds new configs to the list. */
	public void addConfigs( IConfig... configs ) {
		this.configTypeList.addAll( Arrays.asList( configs ) );
	}
}
