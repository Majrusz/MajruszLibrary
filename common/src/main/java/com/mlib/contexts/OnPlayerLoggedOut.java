package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class OnPlayerLoggedOut {
	public final ServerPlayer player;

	public static Context< OnPlayerLoggedOut > listen( Consumer< OnPlayerLoggedOut > consumer ) {
		return Contexts.get( OnPlayerLoggedOut.class ).add( consumer );
	}

	public OnPlayerLoggedOut( ServerPlayer player ) {
		this.player = player;
	}
}
