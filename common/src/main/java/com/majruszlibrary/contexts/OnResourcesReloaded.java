package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;

import java.util.function.Consumer;

public class OnResourcesReloaded {
	public static Context< OnResourcesReloaded > listen( Consumer< OnResourcesReloaded > consumer ) {
		return Contexts.get( OnResourcesReloaded.class ).add( consumer );
	}

	public OnResourcesReloaded() {}
}
