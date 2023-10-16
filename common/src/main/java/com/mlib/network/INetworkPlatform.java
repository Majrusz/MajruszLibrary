package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.modhelper.ModHelper;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface INetworkPlatform {
	void register( ModHelper helper, List< NetworkObject< ? > > objects );

	< Type extends ISerializable > void sendToServer( NetworkObject< Type > object, Type message );

	< Type extends ISerializable > void sendToClients( NetworkObject< Type > object, Type message, List< ServerPlayer > players );
}
