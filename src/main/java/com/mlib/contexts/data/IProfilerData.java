package com.mlib.contexts.data;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public interface IProfilerData {
	ProfilerFiller getProfiler();

	interface Client extends IProfilerData {
		default ProfilerFiller getProfiler() {
			return DistExecutor.unsafeCallWhenOn( Dist.CLIENT, ()->()->Minecraft.getInstance().getProfiler() );
		}
	}
}
