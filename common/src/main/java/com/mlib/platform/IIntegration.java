package com.mlib.platform;

import java.net.URL;
import java.util.Optional;

public interface IIntegration {
	boolean isLoaded( String modId );

	Optional< URL > getUpdateURL( String modId );

	String getName( String modId );

	String getVersion( String modId );
}
