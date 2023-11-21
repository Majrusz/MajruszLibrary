package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class OnDimensionChanged implements IEntityEvent {
	public final ServerPlayer player;
	public final ServerLevel old;
	public final ServerLevel current;

	public static Event< OnDimensionChanged > listen( Consumer< OnDimensionChanged > consumer ) {
		return Events.get( OnDimensionChanged.class ).add( consumer );
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
