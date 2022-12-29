package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfigurable {
	String getName();

	String getComment();

	default boolean requiresWorldRestart() {
		return false;
	}

	default boolean isBuilt() {
		return false;
	}

	default void build( ForgeConfigSpec.Builder builder ) {
		String comment = this.buildComment();
		if( !comment.equals( "" ) ) {
			builder.comment( comment );
		}
		if( this.requiresWorldRestart() ) {
			builder.worldRestart();
		}
	}

	private String buildComment() {
		String comment = this.getComment();
		if( !comment.isEmpty() && this.requiresWorldRestart() ) {
			if( comment.endsWith( "." ) ) {
				return comment.substring( 0, comment.length() - 1 ) + " (requires world/game restart).";
			} else {
				return comment + " (requires world/game restart)";
			}
		}

		return comment;
	}
}
