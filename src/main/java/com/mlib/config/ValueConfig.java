package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public abstract class ValueConfig< Type > extends UserConfig implements Supplier< Type > {
	protected final boolean requiresWorldRestart;
	protected final Type defaultValue;
	protected ForgeConfigSpec.ConfigValue< Type > config = null;

	public ValueConfig( String name, String comment, boolean requiresWorldRestart, Type defaultValue ) {
		super( name, validateComment( comment, requiresWorldRestart ) );
		this.requiresWorldRestart = requiresWorldRestart;
		this.defaultValue = defaultValue;
	}

	@Override
	public void build( ForgeConfigSpec.Builder builder ) {
		if( !this.comment.equals( "" ) ) {
			builder.comment( this.comment );
		}

		if( this.requiresWorldRestart ) {
			builder.worldRestart();
		}
	}

	/** Returns stored (or cached) value. */
	@Override
	public Type get() {
		assert this.config != null : "Config has not been initialized yet!";
		return this.config.get();
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
