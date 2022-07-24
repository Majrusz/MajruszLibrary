package com.mlib.config;

import com.mlib.events.ConfigsLoadedEvent;
import com.mlib.gamemodifiers.GameModifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/** Handler that makes creating new configs much easier. */
public class ConfigHandler {
	public ForgeConfigSpec configSpec;
	protected List< UserConfig > configs = new ArrayList<>();
	private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	private final ModConfig.Type type;
	private final String filename;

	public ConfigHandler( ModConfig.Type type, String filename, String modId ) {
		this.type = type;
		this.filename = modId + "-" + filename;
	}

	/** Registers all configs (all config values are valid after this call). */
	public void register( final ModLoadingContext modLoadingContext ) {
		for( UserConfig config : this.configs )
			config.build( this.builder );

		this.configSpec = this.builder.build();
		modLoadingContext.registerConfig( this.type, this.configSpec, this.filename );
		MinecraftForge.EVENT_BUS.post( new ConfigsLoadedEvent( this ) );
	}

	public < ConfigType extends UserConfig > ConfigType addConfig( ConfigType config ) {
		this.configs.add( config );

		return config;
	}

	public void addConfigs( UserConfig... configs ) {
		this.configs.addAll( List.of( configs ) );
	}

	public < ConfigGroupType extends ConfigGroup > ConfigGroupType addGroup( ConfigGroupType configGroup ) {
		this.configs.add( configGroup );

		return configGroup;
	}

	public ConfigGroup addNewGroup( String groupName, String comment, UserConfig... configs ) {
		ConfigGroup configGroup = new ConfigGroup( groupName, comment, configs );
		this.configs.add( configGroup );

		return configGroup;
	}

	public ConfigGroup addNewGameModifierGroup( String configKey ) {
		ConfigGroup configGroup = GameModifier.addNewGroup( configKey );
		this.configs.add( configGroup );

		return configGroup;
	}

	@Deprecated
	public ConfigGroup addNewGameModifierGroup( String configKey, String groupName, String comment ) {
		ConfigGroup configGroup = GameModifier.addNewGroup( configKey, groupName, comment );
		this.configs.add( configGroup );

		return configGroup;
	}

	public ModConfig.Type getType() {
		return this.type;
	}

	public String getFilename() {
		return this.filename;
	}
}
