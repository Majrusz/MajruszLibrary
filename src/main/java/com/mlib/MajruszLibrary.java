package com.mlib;

import com.mlib.config.AvailabilityConfig;
import com.mlib.config.ConfigGroup;
import com.mlib.config.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 Main class for the whole library.

 @author Majrusz
 @since 2020-01-14 */
@Mod( MajruszLibrary.MOD_ID )
public class MajruszLibrary {
	public static final String MOD_ID = "majrusz_library";
	public static final String NAME = "Majrusz Library";
	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Random RANDOM = new Random();
	public static final ConfigHandler CONFIG_HANDLER = new ConfigHandler( ModConfig.Type.COMMON, "mlib-common.toml" );
	public static final ConfigGroup AVAILABILITY_GROUP = CONFIG_HANDLER.addConfigGroup( new ConfigGroup( "Features", "" ) );

	public static final AvailabilityConfig IS_ENABLED = AVAILABILITY_GROUP.addConfig( new AvailabilityConfig( "is_enabled", "", true, true ) );
	public static final AvailabilityConfig IS_WEIRD = AVAILABILITY_GROUP.addConfig( new AvailabilityConfig("is_weird", "", true, false ) );
	public static final ConfigGroup EXTRA_GROUP = AVAILABILITY_GROUP.addGroup( new ConfigGroup( "2222", "" ) );
	public static final AvailabilityConfig IS_SMALL = EXTRA_GROUP.addConfig( new AvailabilityConfig( "is_small", "", true, false ) );
	public static final ConfigGroup EXTRA_GROUP2 = AVAILABILITY_GROUP.addGroup( new ConfigGroup( "3333", "" ) );
	public static final ConfigGroup EXTRA_GROUP3 = AVAILABILITY_GROUP.addGroup( new ConfigGroup( "4444", "" ) );
	public static final AvailabilityConfig IS_STRONK = EXTRA_GROUP3.addConfig( new AvailabilityConfig( "is_stronk", "", true, false ) );

	public MajruszLibrary() {
		CONFIG_HANDLER.register( ModLoadingContext.get() );

		LOGGER.debug( "IS_ENABLED: " + IS_ENABLED.isEnabled() );
		LOGGER.debug( "IS_WEIRD: " + IS_WEIRD.isEnabled() );
		LOGGER.debug( "IS_SMALL: " + IS_SMALL.isEnabled() );
		LOGGER.debug( "IS_STRONK: " + IS_STRONK.isEnabled() );

		MinecraftForge.EVENT_BUS.register( this );
	}
}
