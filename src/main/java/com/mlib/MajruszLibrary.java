package com.mlib;

import com.mlib.commands.CommandBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Random RANDOM = new Random();

	public MajruszLibrary() {
		MinecraftForge.EVENT_BUS.register( this );

		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		forgeEventBus.addListener( CommandBase::registerAll );
	}

	/** Returns resource location for register in current modification files. */
	public static ResourceLocation getLocation( String register ) {
		return new ResourceLocation( MOD_ID, register );
	}
}
