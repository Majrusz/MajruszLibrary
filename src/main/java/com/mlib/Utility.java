package com.mlib;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

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

	public static ResourceLocation getRegistryKey( ItemStack itemStack ) {
		return getRegistryKey( itemStack.getItem() );
	}

	public static String getRegistryString( ItemStack itemStack ) {
		return getRegistryString( itemStack.getItem() );
	}

	public static ResourceLocation getRegistryKey( EntityType< ? > entityType ) {
		return EntityType.getKey( entityType );
	}

	public static String getRegistryString( EntityType< ? > entityType ) {
		return getRegistryKey( entityType ).toString();
	}

	public static ResourceLocation getRegistryKey( LivingEntity entity ) {
		return getRegistryKey( entity.getType() );
	}

	public static String getRegistryString( LivingEntity entity ) {
		return getRegistryString( entity.getType() );
	}

	public static ResourceLocation getRegistryKey( Enchantment enchantment ) {
		return Registry.ENCHANTMENT.getKey( enchantment );
	}

	public static String getRegistryString( Enchantment enchantment ) {
		return getRegistryKey( enchantment ).toString();
	}

	public static ResourceLocation getRegistryKey( Level level ) {
		return level.dimension().location();
	}

	public static String getRegistryString( Level level ) {
		return getRegistryKey( level ).toString();
	}

	public static String getPlayerUUID( Player player ) {
		return String.valueOf( player.getUUID() );
	}

	public static boolean isServerSide() {
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
	}

	public static boolean isClientSide() {
		return Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT;
	}

	public static boolean isProduction() {
		return FMLEnvironment.production;
	}
}
