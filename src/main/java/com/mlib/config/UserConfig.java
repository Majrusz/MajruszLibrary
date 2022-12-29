package com.mlib.config;

public abstract class UserConfig implements IConfigurable {
	protected String name;
	protected String comment;
	protected boolean worldRestartRequired;

	public UserConfig( String name, String comment, boolean worldRestartRequired ) {
		this.name = name;
		this.comment = comment;
		this.worldRestartRequired = worldRestartRequired;
	}

	public UserConfig( String name, String comment ) {
		this( name, comment, false );
	}

	public UserConfig() {
		this( "", "" );
	}

	public UserConfig name( String name ) {
		this.name = name;

		return this;
	}

	public UserConfig comment( String comment ) {
		this.comment = comment;

		return this;
	}

	public UserConfig requiresWorldRestart( boolean worldRestartRequired ) {
		this.worldRestartRequired = worldRestartRequired;

		return this;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getComment() {
		return this.comment;
	}

	@Override
	public boolean requiresWorldRestart() {
		return this.worldRestartRequired;
	}
}
