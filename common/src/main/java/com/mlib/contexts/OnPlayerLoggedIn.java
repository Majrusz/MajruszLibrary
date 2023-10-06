package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class OnPlayerLoggedIn {
	public final ServerPlayer player;

	public static Context< OnPlayerLoggedIn > listen( Consumer< OnPlayerLoggedIn > consumer ) {
		return Contexts.get( OnPlayerLoggedIn.class ).add( consumer );
	}

	public OnPlayerLoggedIn( ServerPlayer player ) {
		this.player = player;
	}
}
