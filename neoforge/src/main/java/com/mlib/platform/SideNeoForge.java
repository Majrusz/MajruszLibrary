package com.mlib.platform;

import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class SideNeoForge implements ISidePlatform {
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
	public boolean canLoadClassOnServer( String annotations ) {
		return !annotations.contains( "Lcom/mlib/annotation/Dist;" )
			&& !annotations.contains( "Lnet/neoforged/api/distmarker/Dist;" )
			|| !annotations.contains( "CLIENT" );
	}

	@Override
	public MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}
}
