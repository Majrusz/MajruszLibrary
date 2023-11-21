package com.majruszlibrary.events.type;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface ILevelEvent {
	Level getLevel();

	default ServerLevel getServerLevel() {
		assert this.getLevel() instanceof ServerLevel;

		return ( ServerLevel )this.getLevel();
	}
}
