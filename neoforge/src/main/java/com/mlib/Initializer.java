package com.mlib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod( MajruszLibrary.MOD_ID )
public class Initializer {
	public Initializer() {
		MajruszLibrary.HELPER.register();
		MinecraftForge.EVENT_BUS.register( this );
	}
}
