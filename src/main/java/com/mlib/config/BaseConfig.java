package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

/** Class representing single config value. */
public abstract class BaseConfig implements IConfig {
	protected final String name;
	protected final String comment;
	protected final boolean requiresWorldRestart;

	public BaseConfig( String name, String comment, boolean requiresWorldRestart ) {
		this.name = name;
		this.comment = comment;
		this.requiresWorldRestart = requiresWorldRestart;
	}

	/** Builds current config. */
	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		if( !this.comment.equals( "" ) )
			builder.comment( this.comment );

		if( this.requiresWorldRestart )
			builder.worldRestart();
	}
}
