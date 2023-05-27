package com.mlib.gamemodifiers.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface ILevelData {
	Level getLevel();

	default ServerLevel getServerLevel() {
		assert this.getLevel() instanceof ServerLevel;

		return ( ServerLevel )this.getLevel();
	}
}
