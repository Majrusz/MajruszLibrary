package com.mlib.mixin;

import com.mlib.contexts.OnPlayerLoggedIn;
import com.mlib.contexts.OnPlayerLoggedOut;
import com.mlib.contexts.base.Contexts;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( PlayerList.class )
public abstract class MixinPlayerList {
	@Inject(
		at = @At( "RETURN" ),
		method = "placeNewPlayer (Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V"
	)
	private void placeNewPlayer( Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo callback ) {
		Contexts.dispatch( new OnPlayerLoggedIn( player ) );
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "remove (Lnet/minecraft/server/level/ServerPlayer;)V"
	)
	private void remove( ServerPlayer player, CallbackInfo callback ) {
		Contexts.dispatch( new OnPlayerLoggedOut( player ) );
	}
}
