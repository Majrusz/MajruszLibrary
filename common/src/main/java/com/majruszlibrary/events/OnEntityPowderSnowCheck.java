package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

public class OnEntityPowderSnowCheck implements IEntityEvent {
	public final Entity entity;
	private boolean canWalk;

	public static Event< OnEntityPowderSnowCheck > listen( Consumer< OnEntityPowderSnowCheck > consumer ) {
		return Events.get( OnEntityPowderSnowCheck.class ).add( consumer );
	}

	public OnEntityPowderSnowCheck( Entity entity, boolean canWalk ) {
		this.entity = entity;
		this.canWalk = canWalk;
	}

	@Override
	public Entity getEntity() {
		return this.entity;
	}

	public void makeWalkable() {
		this.canWalk = true;
	}

	public boolean canWalk() {
		return this.canWalk;
	}
}
