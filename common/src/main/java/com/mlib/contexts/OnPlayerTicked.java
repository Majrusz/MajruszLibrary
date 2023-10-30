package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnPlayerTicked implements IEntityData {
	public final Player player;

	public static Context< OnPlayerTicked > listen( Consumer< OnPlayerTicked > consumer ) {
		return Contexts.get( OnPlayerTicked.class ).add( consumer );
	}

	public OnPlayerTicked( Player player ) {
		this.player = player;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}
}
