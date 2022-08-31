package com.mlib.config;

public abstract class UserConfig implements IConfigurable {
	protected final String name;
	protected final String comment;
	protected final boolean worldRestartRequired;

	public UserConfig( String name, String comment, boolean worldRestartRequired ) {
		this.name = name;
		this.comment = validateComment( comment, worldRestartRequired );
		this.worldRestartRequired = worldRestartRequired;
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

	private static String validateComment( String comment, boolean requiresWorldRestart ) {
		if( !comment.isEmpty() && requiresWorldRestart ) {
			if( comment.endsWith( "." ) ) {
				return comment.substring( 0, comment.length() - 1 ) + " (requires world/game restart).";
			} else {
				return comment + " (requires world/game restart)";
			}
		}

		return comment;
	}
}
