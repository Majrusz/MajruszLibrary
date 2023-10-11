package com.mlib.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public interface ISidePlatform {
	default void run( Supplier< Runnable > logicalClient, Supplier< Runnable > logicalServer ) {
		( this.isLogicalClient() ? logicalClient : logicalServer ).get().run();
	}

	default < Type > Type get( Supplier< Supplier< Type > > logicalClient, Supplier< Supplier< Type > > logicalServer ) {
		return ( this.isLogicalClient() ? logicalClient : logicalServer ).get().get();
	}

	default void runOnClient( Supplier< Runnable > supplier ) {
		if( this.isClient() ) {
			supplier.get().run();
		}
	}

	default boolean isAuthority() {
		return this.getServer() != null; // server can be dedicated or integrated with the client
	}

	default boolean isLogicalClient() {
		return this.isClient()
			&& this.getMinecraft().isSameThread();
	}

	boolean isDevBuild();

	boolean isDedicatedServer();

	boolean isClient();

	MinecraftServer getServer();

	Minecraft getMinecraft();

	Level getClientLevel();
}
