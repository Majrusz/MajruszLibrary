package com.mlib.platform;

import net.fabricmc.loader.api.FabricLoader;

public class IntegrationFabric implements IIntegration {
	@Override
	public boolean isLoaded( String modId ) {
		return FabricLoader.getInstance().isModLoaded( modId );
	}
}
