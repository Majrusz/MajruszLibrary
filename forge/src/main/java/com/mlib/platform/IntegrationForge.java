package com.mlib.platform;

import net.minecraftforge.fml.ModList;

public class IntegrationForge implements IIntegration {
	@Override
	public boolean isLoaded( String modId ) {
		return ModList.get().isLoaded( modId );
	}
}
