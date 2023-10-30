package com.mlib.platform;

import net.minecraftforge.fml.ModList;

import java.net.URL;
import java.util.Optional;

public class IntegrationNeoForge implements IIntegration {
	@Override
	public boolean isLoaded( String modId ) {
		return ModList.get().isLoaded( modId );
	}

	@Override
	public Optional< URL > getUpdateURL( String modId ) {
		return ModList.get().getModContainerById( modId ).flatMap( mod->mod.getModInfo().getUpdateURL() );
	}

	@Override
	public String getName( String modId ) {
		return ModList.get().getModContainerById( modId ).map( mod->mod.getModInfo().getDisplayName() ).orElseThrow();
	}

	@Override
	public String getVersion( String modId ) {
		return ModList.get().getModContainerById( modId ).map( mod->mod.getModInfo().getVersion().toString() ).orElseThrow();
	}
}
