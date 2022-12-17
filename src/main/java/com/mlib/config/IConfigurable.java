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
		String comment = this.getComment();
		if( !comment.equals( "" ) ) {
			builder.comment( comment );
		}
		if( this.requiresWorldRestart() ) {
			builder.worldRestart();
		}
	}
}
