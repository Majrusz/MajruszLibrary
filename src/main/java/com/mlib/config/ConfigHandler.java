package com.mlib.config;

import com.mlib.MajruszLibrary;
import com.mlib.gamemodifiers.contexts.OnConfigLoaded;
import com.mlib.modhelper.ModHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/** Handler that makes creating new configs much easier. */
public class ConfigHandler extends ConfigGroup {
	final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	final ModConfig.Type type;
	ForgeConfigSpec configSpec = null;

	public ConfigHandler( ModConfig.Type type ) {
		this.type = type;
	}

	@Deprecated
	public void register( final ModLoadingContext modLoadingContext ) {
		this.build( this.builder );

		this.configSpec = this.builder.build();
		modLoadingContext.registerConfig( this.type, this.configSpec );
		OnConfigLoaded.dispatch( this );
		if( this.type == ModConfig.Type.SERVER && this.configSpec.size() > 0 ) {
			this.registerHelpConfigSpec( modLoadingContext );
		}
		FMLJavaModLoadingContext.get().getModEventBus().addListener( this::onConfigReload );
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
		helper.getEventBus().addListener( this::onConfigReload );
	}

	public ModConfig.Type getType() {
		return this.type;
	}

	private void onConfigReload( ModConfigEvent.Reloading event ) {
		MajruszLibrary.log( "config %s", event.getConfig().getModId() );
	}

	private void registerHelpConfigSpec( final ModLoadingContext modLoadingContext ) {
		String[] comments = {
			" Hello, Majrusz over here!",
			" Configs used by my mods are mostly server-side which means they are stored",
			" separately per world. Thanks to this change, configuration files are synchronised",
			" with other players on the server, which is crucial for some of the functionalities.",
			"  ",
			" Anyway, here are the exact locations for 'true' configuration files:",
			"   Singleplayer: %appdata%/saves/<world>/serverconfig",
			"   Multiplayer: <your server>/<world>/serverconfig",
			"  ",
			" Remember that server-side configs can be used on all newly created",
			" worlds when you just copy them to 'defaultconfigs' folder inside '.minecraft'.",
			"  ",
			" If you have any questions or want to see some more configs, feel",
			" free to contact me on my Discord server! Hope to see you there :D"
		};

		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.comment( comments ).define( "discord_url", "https://discord.gg/9UF774WcuW" );
		modLoadingContext.registerConfig( ModConfig.Type.COMMON, builder.build() );
	}
}
