package com.mlib.platform;

import net.minecraftforge.fml.ModList;

public class IntegrationNeoForge implements IIntegration {
	@Override
	public boolean isLoaded( String modId ) {
		return ModList.get().isLoaded( modId );
	}
}
