package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class OnPlayerLoggedOut implements IEntityData {
	public final ServerPlayer player;

	public static Context< OnPlayerLoggedOut > listen( Consumer< OnPlayerLoggedOut > consumer ) {
		return Contexts.get( OnPlayerLoggedOut.class ).add( consumer );
	}

	public OnPlayerLoggedOut( ServerPlayer player ) {
		this.player = player;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
