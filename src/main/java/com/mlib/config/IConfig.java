package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfig {
	/** Builds current config or all configs from the list. */
	void build( ForgeConfigSpec.Builder builder );
}
