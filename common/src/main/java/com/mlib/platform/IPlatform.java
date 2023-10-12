package com.mlib.platform;

import net.minecraft.server.MinecraftServer;

public interface IPlatform {
	boolean isDevBuild();

	boolean isDedicatedServer();

	boolean isClient();

	MinecraftServer getServer();
}
