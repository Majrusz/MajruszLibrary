package com.majruszlibrary.mixininterfaces;

import java.util.function.Consumer;

public interface IMixinClientLevel {
	< Type > void majruszlibrary$delayExecution( int entityId, Class< Type > clazz, Consumer< Type > consumer );
}
