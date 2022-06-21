package com.mlib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
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
	public static final String MOD_ID = "majrusz_library";
	public static final String NAME = "Majrusz Library";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final RandomSource RANDOM = RandomSource.create();

	public MajruszLibrary() {
		Registries.initialize();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
