package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnDimensionChanged implements IEntityData {
	public final ServerPlayer player;
	public final ServerLevel old;
	public final ServerLevel current;

	public static Context< OnDimensionChanged > listen( Consumer< OnDimensionChanged > consumer ) {
		return Contexts.get( OnDimensionChanged.class ).add( consumer );
	}

	public OnDimensionChanged( ServerPlayer player, ServerLevel old, ServerLevel current ) {
		this.player = player;
		this.old = old;
		this.current = current;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public boolean is( ResourceKey< Level > level ) {
		return this.current.dimension().equals( level );
	}
}
