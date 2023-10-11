package com.mlib.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class ModSideFabric implements ISidePlatform {
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

	@Override
	@Environment( EnvType.CLIENT )
	public Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}

	@Override
	@Environment( EnvType.CLIENT )
	public Level getClientLevel() {
		return this.getMinecraft().level;
	}
}
