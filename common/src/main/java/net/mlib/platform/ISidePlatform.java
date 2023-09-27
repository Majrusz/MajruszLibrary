package net.mlib.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

import java.util.function.Supplier;

public interface ISidePlatform {
	default void runOnClient( Supplier< Runnable > supplier ) {
		if( this.isClient() ) {
			supplier.get().run();
		}
	}

	boolean isDevBuild();

	boolean isServer();

	boolean isClient();

	MinecraftServer getServer();

	Minecraft getMinecraft();
}
