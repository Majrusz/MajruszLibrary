package com.mlib.platform;

import com.mlib.annotation.Dist;
import com.mlib.annotation.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.function.Supplier;

public class Platform {
	private static final IPlatform PLATFORM = Services.load( IPlatform.class );

	public static void run( Supplier< Runnable > logicalClient, Supplier< Runnable > logicalServer ) {
		( Platform.isLogicalClient() ? logicalClient : logicalServer ).get().run();
	}

	public static < Type > Type get( Supplier< Supplier< Type > > logicalClient, Supplier< Supplier< Type > > logicalServer ) {
		return ( Platform.isLogicalClient() ? logicalClient : logicalServer ).get().get();
	}

	public static void runOnClient( Supplier< Runnable > supplier ) {
		if( Platform.isClient() ) {
			supplier.get().run();
		}
	}

	public static boolean isAuthority() {
		return PLATFORM.getServer() != null; // server can be dedicated or integrated with the client
	}

	public static boolean isLogicalClient() {
		return Platform.isClient()
			&& Platform.getMinecraft().isSameThread();
	}

	public static boolean isLogicalServer() {
		return !Platform.isLogicalClient();
	}

	public static boolean isDevBuild() {
		return PLATFORM.isDevBuild();
	}

	public static boolean isDedicatedServer() {
		return PLATFORM.isDedicatedServer();
	}

	public static boolean isClient() {
		return PLATFORM.isClient();
	}

	public static MinecraftServer getServer() {
		return PLATFORM.getServer();
	}

	@OnlyIn( Dist.CLIENT )
	public static Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}

	@OnlyIn( Dist.CLIENT )
	public static ClientLevel getLocalLevel() {
		return Minecraft.getInstance().level;
	}

	@OnlyIn( Dist.CLIENT )
	public static LocalPlayer getLocalPlayer() {
		return Minecraft.getInstance().player;
	}
}
