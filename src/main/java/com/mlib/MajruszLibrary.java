package com.mlib;

import com.mlib.config.ConfigGroup;
import com.mlib.config.ConfigHandler;
import com.mlib.gamemodifiers.GameModifier;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 Main class for the whole library.

 @author Majrusz
 @since 2020-01-14 */
@Mod( MajruszLibrary.MOD_ID )
public class MajruszLibrary {
	public static final String MOD_ID = "majruszlib";
	public static final String NAME = "Majrusz Library";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final RandomSource RANDOM = RandomSource.create();
	public static final ConfigHandler CONFIG_HANDLER = new ConfigHandler( ModConfig.Type.COMMON, "common.toml", MOD_ID );

	public MajruszLibrary() {
		CONFIG_HANDLER.addNewGameModifierGroup( GameModifier.DEFAULT_KEY, "", "" );
		Registries.initialize();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
