package com.mlib.time;

public interface IAnimation {
	default void onStart() {}

	default void onTick() {}

	default void onEnd() {}

	boolean isFinished();
}
