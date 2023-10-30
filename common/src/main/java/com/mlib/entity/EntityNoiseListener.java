package com.mlib.entity;

import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import net.minecraft.world.entity.Entity;

public class EntityNoiseListener {
	private static final Long2BooleanMap CLASSES = new Long2BooleanOpenHashMap();

	public synchronized static void add( Class< ? extends Entity > clazz ) {
		CLASSES.putIfAbsent( clazz.hashCode(), true );
	}

	public static boolean isSupported( Class< ? extends Entity > clazz ) {
		return CLASSES.getOrDefault( clazz.hashCode(), false );
	}
}
