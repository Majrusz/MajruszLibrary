package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Represents a group that contains many other configs and/or groups. */
public class ConfigGroup extends UserConfig {
	final List< IConfigurable > configs = new ArrayList<>();

	public ConfigGroup( IConfigurable... configs ) {
		this.addConfigs( configs );
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

	@Override
	public boolean isBuilt() {
		return this.configs.stream().allMatch( IConfigurable::isBuilt );
	}

	@Override
	public ConfigGroup name( String name ) {
		super.name( name );

		return this;
	}

	@Override
	public ConfigGroup comment( String comment ) {
		super.comment( comment );

		return this;
	}

	@Override
	public ConfigGroup requiresWorldRestart( boolean worldRestartRequired ) {
		super.requiresWorldRestart( worldRestartRequired );

		return this;
	}

	public ConfigGroup addConfig( IConfigurable config ) {
		this.configs.add( config );

		return this;
	}

	public ConfigGroup addConfigs( IConfigurable... configs ) {
		Arrays.asList( configs ).forEach( this::addConfig );

		return this;
	}

	public List< IConfigurable > getConfigs() {
		return Collections.unmodifiableList( this.configs );
	}
}
