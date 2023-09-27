package net.mlib.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class ModSideFabric implements ISidePlatform {
	public static MinecraftServer SERVER;

	@Override
	public boolean isDevBuild() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isServer() {
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

	@Override
	@Environment( EnvType.CLIENT )
	public Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}
}
