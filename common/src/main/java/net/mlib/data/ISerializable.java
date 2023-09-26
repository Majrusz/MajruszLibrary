package net.mlib.data;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface ISerializable {
	void read( JsonElement element );

	void write( FriendlyByteBuf buffer );

	void read( FriendlyByteBuf buffer );

	void write( Tag tag );

	void read( Tag tag );

	default void onServer( ServerData serverData ) {}

	default void onClient( ClientData clientData ) {}

	class ServerData {
		public final MinecraftServer server;
		public final ServerPlayer player;

		public ServerData( MinecraftServer server, ServerPlayer player ) {
			this.server = server;
			this.player = player;
		}
	}

	class ClientData {
		public final Minecraft minecraft;

		public ClientData( Minecraft minecraft ) {
			this.minecraft = minecraft;
		}
	}
}
