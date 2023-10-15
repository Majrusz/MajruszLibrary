package com.mlib.platform;

public class Integration {
	private static final IIntegration PLATFORM = Services.load( IIntegration.class );

	public static boolean isLoaded( String modId ) {
		return PLATFORM.isLoaded( modId );
	}
}
