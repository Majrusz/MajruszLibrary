package com.mlib;

import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

/** Class with common functions for most of the Minecraft's classes. */
public class CommonHelper {
	/** Tries to cast the object to given class type or returns null otherwise. */
	@Nullable
	public static < NewType > NewType castIfPossible( Class< NewType > newClass, Object object ) {
		return newClass.isInstance( object ) ? newClass.cast( object ) : null;
	}

	/** Returns registry name whether possible or null otherwise. */
	@Nullable
	public static < Type extends IForgeRegistryEntry< Type > > String getRegistryNameString( Type entry ) {
		return entry.getRegistryName() != null ? entry.getRegistryName().toString() : null;
	}
}
