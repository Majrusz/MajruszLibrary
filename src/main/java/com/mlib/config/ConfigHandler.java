package com.mlib.config;

import com.mlib.contexts.OnConfigLoaded;
import com.mlib.modhelper.ModHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/** Handler that makes creating new configs much easier. */
public class ConfigHandler extends ConfigGroup {
	final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	final ModConfig.Type type;
	ForgeConfigSpec configSpec = null;

	public ConfigHandler( ModConfig.Type type ) {
		this.type = type;
	}

	public void register( ModHelper helper ) {
		this.build( this.builder );

		ModLoadingContext modLoadingContext = helper.getModLoadingContext();
		this.configSpec = this.builder.build();
		modLoadingContext.registerConfig( this.type, this.configSpec );
		OnConfigLoaded.dispatch( this );
		if( this.type == ModConfig.Type.SERVER && this.configSpec.size() > 0 ) {
			this.registerHelpConfigSpec( modLoadingContext );
		}
		helper.getEventBus().addListener( this::onConfigReloaded );
		helper.getEventBus().addListener( this::onConfigLoaded );
	}

	public ModConfig.Type getType() {
		return this.type;
	}

	private void onConfigReloaded( ModConfigEvent.Reloading event ) {
		this.configs.forEach( IConfigurable::onReload );
	}

	private void onConfigLoaded( ModConfigEvent.Loading event ) {
		this.configs.forEach( IConfigurable::onReload );
	}

	private void registerHelpConfigSpec( final ModLoadingContext modLoadingContext ) {
		String[] comments = {
			" Hello, Majrusz over here!",
			" Configs used by my mods are mostly server-side which means they are stored",
			" separately per world. Thanks to this change, configuration files are synchronised",
			" with other players on the server, which is crucial for some of the functionalities.",
			"  ",
			" Anyway, here are the exact locations for 'true' configuration files:",
			"   Singleplayer: %appdata%/.minecraft/saves/<world>/serverconfig",
			"   Multiplayer: <your server>/<world>/serverconfig",
			"  ",
			" Remember that server-side configs can be used on all newly created",
			" worlds when you copy them to folder: %appdata%/.minecraft/defaultconfigs",
			"  ",
			" If you have any questions or want to see some more configs, feel",
			" free to contact me on my Discord server! Hope to see you there :D"
		};

		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment( comments ).define( "discord_url", "https://discord.gg/9UF774WcuW" );
		modLoadingContext.registerConfig( ModConfig.Type.COMMON, builder.build() );
	}
}
