package com.mlib.platform;

import java.net.URL;
import java.util.Optional;

public class Integration {
	private static final IIntegration PLATFORM = Services.load( IIntegration.class );

	public static boolean isLoaded( String modId ) {
		return PLATFORM.isLoaded( modId );
	}

	public static Optional< URL > getUpdateURL( String modId ) {
		return PLATFORM.getUpdateURL( modId );
	}

	public static String getName( String modId ) {
		return PLATFORM.getName( modId );
	}

	public static String getVersion( String modId ) {
		return PLATFORM.getVersion( modId );
	}

	public static String getMinecraftVersion() {
		return "1.20.1";
	}
}
