package com.mlib.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ModSideForge implements ISidePlatform {
	@Override
	public boolean isDevBuild() {
		return !FMLEnvironment.production;
	}

	@Override
	public boolean isDedicatedServer() {
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}

	@Override
	public boolean isClient() {
		return FMLEnvironment.dist == Dist.CLIENT;
	}

	@Override
	public MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public Level getClientLevel() {
		return this.getMinecraft().level;
	}
}
