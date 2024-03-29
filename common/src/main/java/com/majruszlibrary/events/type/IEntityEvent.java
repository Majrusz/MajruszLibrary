package com.majruszlibrary.events.type;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IEntityEvent extends ILevelEvent, IPositionEvent {
	Entity getEntity();

	default Level getLevel() {
		return this.getEntity().level();
	}

	default Vec3 getPosition() {
		return this.getEntity().position();
	}
}
