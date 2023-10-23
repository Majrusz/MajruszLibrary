package com.mlib.platform;

import net.minecraft.server.MinecraftServer;

public interface ISidePlatform {
	boolean isDevBuild();

	boolean isDedicatedServer();

	boolean isClient();

	boolean canLoadClassOnServer( String annotations );

	MinecraftServer getServer();
}
