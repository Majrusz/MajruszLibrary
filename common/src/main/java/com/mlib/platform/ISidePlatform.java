package com.mlib.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import java.util.function.Supplier;

public interface ISidePlatform {
	default void runOnClient( Supplier< Runnable > supplier ) {
		if( this.isClient() ) {
			supplier.get().run();
		}
	}

	default boolean isAuthority() {
		return this.getServer() != null; // server can be dedicated or integrated with the client
	}

	boolean isDevBuild();

	boolean isDedicatedServer();

	boolean isClient();

	MinecraftServer getServer();

	Minecraft getMinecraft();
}
