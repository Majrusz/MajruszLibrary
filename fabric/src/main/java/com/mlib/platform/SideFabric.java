package com.mlib.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class SideFabric implements ISidePlatform {
	public static MinecraftServer SERVER;

	@Override
	public boolean isDevBuild() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isDedicatedServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	@Override
	public boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public MinecraftServer getServer() {
		return SERVER;
	}
}
