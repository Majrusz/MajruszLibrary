package com.majruszlibrary.platform;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

public class SideForge implements ISidePlatform {
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
		return !annotations.contains( "Lcom/majruszlibrary/annotation/Dist;" )
			&& !annotations.contains( "Lnet/minecraftforge/api/distmarker/Dist;" )
			|| !annotations.contains( "CLIENT" );
	}

	@Override
	public MinecraftServer getServer() {
		return ServerLifecycleHooks.getCurrentServer();
	}
}
