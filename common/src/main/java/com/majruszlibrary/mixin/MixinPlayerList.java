package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnPlayerLoggedIn;
import com.majruszlibrary.events.OnPlayerLoggedOut;
import com.majruszlibrary.events.base.Events;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( PlayerList.class )
public abstract class MixinPlayerList {
	@Inject(
		at = @At( "RETURN" ),
		method = "placeNewPlayer (Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V"
	)
	private void placeNewPlayer( Connection connection, ServerPlayer player, CallbackInfo callback ) {
		Events.dispatch( new OnPlayerLoggedIn( player ) );
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "remove (Lnet/minecraft/server/level/ServerPlayer;)V"
	)
	private void remove( ServerPlayer player, CallbackInfo callback ) {
		Events.dispatch( new OnPlayerLoggedOut( player ) );
	}
}
