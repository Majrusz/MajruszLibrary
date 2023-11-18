package com.mlib.mixininterfaces;

import java.util.function.Consumer;

public interface IMixinClientLevel {
	< Type > void mlib$delayExecution( int entityId, Class< Type > clazz, Consumer< Type > consumer );
}
