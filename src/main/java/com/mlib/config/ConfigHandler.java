package com.mlib.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/** Class removing redundancy and repetitive fragments of code. */
public class ConfigHandler {
	public ForgeConfigSpec configSpec;
	protected List< ConfigGroup > configGroupList = new ArrayList<>();
	private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	private final ModConfig.Type type;
	private final String filename;

	public ConfigHandler( ModConfig.Type type, String filename ) {
		this.type = type;
		this.filename = filename;
	}

	/** Final register. After this command every config values should have appropriate value. */
	public void register( final ModLoadingContext modLoadingContext ) {
		for( ConfigGroup configGroup : this.configGroupList )
			configGroup.build( this.builder );

		this.configSpec = this.builder.build();

		modLoadingContext.registerConfig( this.type, this.configSpec, this.filename );
	}

	/** Adds a new config group to handle. */
	public < ConfigGroupType extends ConfigGroup > ConfigGroupType addConfigGroup( ConfigGroupType configGroup ) {
		this.configGroupList.add( configGroup );

		return configGroup;
	}
}
