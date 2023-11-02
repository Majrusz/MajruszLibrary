package com.mlib;

import net.neoforged.fml.common.Mod;

@Mod( MajruszLibrary.MOD_ID )
public class Initializer {
	public Initializer() {
		MajruszLibrary.HELPER.register();
	}
}
