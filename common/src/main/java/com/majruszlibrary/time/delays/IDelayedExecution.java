package com.majruszlibrary.time.delays;

public interface IDelayedExecution {
	default void start() {}

	default void tick() {}

	default void finish() {}

	boolean isFinished();
}
