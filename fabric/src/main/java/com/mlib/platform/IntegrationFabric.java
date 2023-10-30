package com.mlib.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.net.URL;
import java.util.Optional;

public class IntegrationFabric implements IIntegration {
	@Override
	public boolean isLoaded( String modId ) {
		return FabricLoader.getInstance().isModLoaded( modId );
	}

	@Override
	public Optional< URL > getUpdateURL( String modId ) {
		try {
			Optional< ModContainer > mod = FabricLoader.getInstance().getModContainer( modId );
			if( mod.isPresent() ) {
				ModMetadata metadata = mod.get().getMetadata();
				if( metadata.containsCustomValue( "update" ) ) {
					return Optional.of( new URL( metadata.getCustomValue( "update" ).getAsString() ) );
				}
			}
		} catch( Exception ignored ) {}

		return Optional.empty();
	}

	@Override
	public String getName( String modId ) {
		return FabricLoader.getInstance().getModContainer( modId ).map( mod->mod.getMetadata().getName() ).orElseThrow();
	}

	@Override
	public String getVersion( String modId ) {
		return FabricLoader.getInstance().getModContainer( modId ).map( mod->mod.getMetadata().getVersion().getFriendlyString() ).orElseThrow();
	}
}
