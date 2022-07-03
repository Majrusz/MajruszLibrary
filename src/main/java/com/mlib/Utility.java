package com.mlib;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public class Utility {
	public static final int TICKS_IN_SECOND = 20;
	public static final int TICKS_IN_MINUTE = TICKS_IN_SECOND * 60;

	public static int secondsToTicks( double seconds ) {
		return ( int )( seconds * TICKS_IN_SECOND );
	}

	public static double ticksToSeconds( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_SECOND;
	}

	public static int minutesToTicks( double minutes ) {
		return ( int )( minutes * TICKS_IN_MINUTE );
	}

	public static double ticksToMinutes( int ticks ) {
		return ( double )( ticks ) / TICKS_IN_MINUTE;
	}

	/** Tries to cast the object to given class type or returns null otherwise. */
	@Nullable
	public static < NewType > NewType castIfPossible( Class< NewType > newClass, Object object ) {
		return newClass.isInstance( object ) ? newClass.cast( object ) : null;
	}

	public static ResourceLocation getRegistryKey( Item item ) {
		return Registry.ITEM.getKey( item );
	}

	public static String getRegistryString( Item item ) {
		return getRegistryKey( item ).toString();
	}

	public static ResourceLocation getRegistryKey( EntityType< ? > entityType ) {
		return EntityType.getKey( entityType );
	}

	public static String getRegistryString( EntityType< ? > entityType ) {
		return getRegistryKey( entityType ).toString();
	}

	public static String getPlayerUUID( Player player ) {
		return String.valueOf( player.getUUID() );
	}
}
