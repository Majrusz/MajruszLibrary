package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public abstract class ValueConfig< Type, ConfigType extends ForgeConfigSpec.ConfigValue< Type > > extends UserConfig implements IValueConfig< Type > {
	protected final boolean requiresWorldRestart;
	protected final Type defaultValue;
	protected ConfigType config;

	public ValueConfig( String name, String comment, boolean requiresWorldRestart, Type defaultValue ) {
		super( name, validateComment( comment, requiresWorldRestart ) );
		this.requiresWorldRestart = requiresWorldRestart;
		this.defaultValue = defaultValue;
	}

	public void build( ForgeConfigSpec.Builder builder ) {
		if( !this.comment.equals( "" ) )
			builder.comment( this.comment );

		if( this.requiresWorldRestart )
			builder.worldRestart();

		this.config = buildValue( builder );
		assert this.config != null;
	}

	public ConfigType buildValue( ForgeConfigSpec.Builder builder ) {
		return ( ConfigType )builder.define( this.name, this.defaultValue );
	}

	/** Returns value stored in the configuration file (or cached). */
	@Override
	public Type get() {
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
