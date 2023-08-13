package com.mlib;

import com.mlib.contexts.base.ModConfigs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 Main class for the whole library.

 @author Majrusz
 @since 2020-01-14 */
@Mod( MajruszLibrary.MOD_ID )
public class MajruszLibrary {
	public static final String MOD_ID = "mlib";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ModConfigs MOD_CONFIGS = new ModConfigs();

	public static void log( String format, Object... args ) {
		LOGGER.info( String.format( format, args ) );
	}

	public static void logOnDev( String format, Object... args ) {
		if( Utility.isDevBuild() ) {
			log( format, args );
		}
	}

	public MajruszLibrary() {
		com.mlib.Registries.initialize();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
