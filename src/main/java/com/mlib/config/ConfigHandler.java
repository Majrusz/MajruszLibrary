package com.mlib.config;

import com.mlib.events.ConfigsLoadedEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/** Handler that makes creating new configs much easier. */
public class ConfigHandler {
	public ForgeConfigSpec configSpec;
	protected List< ConfigGroup > configGroups = new ArrayList<>();
	private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	private final ModConfig.Type type;
	private final String filename;

	public ConfigHandler( ModConfig.Type type, String filename ) {
		this.type = type;
		this.filename = filename;
	}

	public ConfigHandler( ModConfig.Type type, String filename, String modId ) {
		this( type, modId + "-" + filename );
	}

	/** Registers all configs (all config values are valid after this call). */
	public void register( final ModLoadingContext modLoadingContext ) {
		for( ConfigGroup configGroup : this.configGroups )
			configGroup.build( this.builder );

		this.configSpec = this.builder.build();
		modLoadingContext.registerConfig( this.type, this.configSpec, this.filename );
		MinecraftForge.EVENT_BUS.post( new ConfigsLoadedEvent( this ) );
	}

	public < ConfigGroupType extends ConfigGroup > ConfigGroupType addGroup( ConfigGroupType configGroup ) {
		this.configGroups.add( configGroup );

		return configGroup;
	}

	public ConfigGroup addNewGroup( String groupName, String comment, UserConfig... configs ) {
		ConfigGroup configGroup = new ConfigGroup( groupName, comment, configs );
		this.configGroups.add( configGroup );

		return configGroup;
	}

	public ModConfig.Type GetType() {
		return this.type;
	}

	public String GetFilename() {
		return this.filename;
	}
}
