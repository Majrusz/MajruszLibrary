package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnInsomniaPhantomsCountGet implements IEntityData {
	public final Player player;
	public final int original;
	public int count;

	public static Context< OnInsomniaPhantomsCountGet > listen( Consumer< OnInsomniaPhantomsCountGet > consumer ) {
		return Contexts.get( OnInsomniaPhantomsCountGet.class ).add( consumer );
	}

	public OnInsomniaPhantomsCountGet( Player player, int count ) {
		this.player = player;
		this.original = count;
		this.count = count;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public int getCount() {
		return Math.max( this.count, 0 );
	}
}
