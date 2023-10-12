package com.mlib.profiler;

import com.mlib.platform.Platform;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.function.Supplier;

public class ProfilerHelper {
	public static ProfilerFiller getProfiler() {
		return Platform.get(
			()->()->Platform.getMinecraft().getProfiler(),
			()->()->Platform.getServer() != null ? Platform.getServer().getProfiler() : InactiveProfiler.INSTANCE
		);
	}

	public static void profile( String name, Runnable runnable ) {
		ProfilerFiller profiler = ProfilerHelper.getProfiler();
		profiler.push( name );
		runnable.run();
		profiler.pop();
	}

	public static < Type > Type profile( String name, Supplier< Type > supplier ) {
		ProfilerFiller profiler = ProfilerHelper.getProfiler();
		profiler.push( name );
		Type value = supplier.get();
		profiler.pop();

		return value;
	}
}
