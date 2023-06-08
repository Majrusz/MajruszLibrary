package com.mlib.gamemodifiers.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;

public interface ILevelData extends IProfilerData {
	Level getLevel();

	default ServerLevel getServerLevel() {
		assert this.getLevel() instanceof ServerLevel;

		return ( ServerLevel )this.getLevel();
	}

	default ProfilerFiller getProfiler() {
		return this.getLevel() != null ? this.getLevel().getProfiler() : InactiveProfiler.INSTANCE;
	}
}
