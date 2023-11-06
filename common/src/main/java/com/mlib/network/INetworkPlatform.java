package com.mlib.network;

import com.mlib.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface INetworkPlatform {
	void register( ModHelper helper, List< NetworkObject< ? > > objects );

	< Type > void sendToServer( NetworkObject< Type > object, Type message );

	< Type > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players );
}
