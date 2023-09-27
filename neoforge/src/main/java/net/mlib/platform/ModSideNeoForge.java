package net.mlib.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ModSideNeoForge implements ISidePlatform {
	@Override
	public boolean isDevBuild() {
		return !FMLEnvironment.production;
	}

	@Override
	public boolean isServer() {
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
}
