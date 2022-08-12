package com.mlib.config;

import com.mlib.events.ConfigsLoadedEvent;
import com.mlib.gamemodifiers.GameModifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/** Handler that makes creating new configs much easier. */
public class ConfigHandler extends ConfigGroup {
	final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	final ModConfig.Type type;
	final String filename;
	ForgeConfigSpec configSpec = null;

	public ConfigHandler( ModConfig.Type type, String filename, String modId ) {
		this.type = type;
		this.filename = modId + "-" + filename;
	}

	/** Registers all configs (all config values are valid after this call). */
	public void register( final ModLoadingContext modLoadingContext ) {
		this.build( this.builder );

		this.configSpec = this.builder.build();
		modLoadingContext.registerConfig( this.type, this.configSpec, this.filename );
		MinecraftForge.EVENT_BUS.post( new ConfigsLoadedEvent( this ) );
	}

	public ModConfig.Type getType() {
		return this.type;
	}

	public String getFilename() {
		return this.filename;
	}
}
