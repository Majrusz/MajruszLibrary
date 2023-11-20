package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class OnEntityNoiseCheck implements IEntityData {
	public final ServerLevel level;
	public final BlockPos position;
	public final Entity listener;
	private boolean isAudible = false;

	public static Context< OnEntityNoiseCheck > listen( Consumer< OnEntityNoiseCheck > consumer ) {
		return Contexts.get( OnEntityNoiseCheck.class ).add( consumer );
	}

	public OnEntityNoiseCheck( ServerLevel level, BlockPos position, Entity listener ) {
		this.level = level;
		this.position = position;
		this.listener = listener;
	}

	@Override
	public Entity getEntity() {
		return this.listener;
	}

	public void makeAudible() {
		this.isAudible = true;
	}

	public boolean isAudible() {
		return this.isAudible;
	}
}
