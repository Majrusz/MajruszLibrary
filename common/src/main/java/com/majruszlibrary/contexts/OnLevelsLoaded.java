package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

public class OnLevelsLoaded {
	public final MinecraftServer server;

	public static Context< OnLevelsLoaded > listen( Consumer< OnLevelsLoaded > consumer ) {
		return Contexts.get( OnLevelsLoaded.class ).add( consumer );
	}

	public OnLevelsLoaded( MinecraftServer server ) {
		this.server = server;
	}
}
