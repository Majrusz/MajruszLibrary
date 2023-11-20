package com.mlib.data;

import com.mlib.math.Range;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class Reader {
	public static IReader< BlockPos > blockPos() {
		return new ReaderBlockPos();
	}

	public static IReader< Boolean > bool() {
		return new ReaderBoolean();
	}

	public static < Type > IReader< Type > custom( Supplier< Type > instance ) {
		return new ReaderCustom<>( instance );
	}

	public static IReader< Enchantment > enchantment() {
		return new ReaderEnchantment();
	}

	public static IReader< EntityType< ? > > entityType() {
		return new ReaderEntityType();
	}

	public static < Type extends Enum< ? > > IReader< Type > enumeration( Supplier< Type[] > values ) {
		return new ReaderEnum<>( values );
	}

	public static IReader< Float > number() {
		return new ReaderFloat();
	}

	public static IReader< Integer > integer() {
		return new ReaderInteger();
	}

	public static < Type > IReader< List< Type > > list( IReader< Type > reader ) {
		return new ReaderList<>( reader );
	}

	public static < Type > IReader< Map< String, Type > > map( IReader< Type > reader ) {
		return new ReaderMap<>( reader );
	}

	public static IReader< MobEffect > mobEffect() {
		return new ReaderMobEffect();
	}

	public static < Type extends Number & Comparable< Type > > IReader< Range< Type > > range( IReader< Type > reader ) {
		return new ReaderRange<>( reader );
	}

	public static IReader< ResourceLocation > location() {
		return new ReaderResourceLocation();
	}

	public static IReader< String > string() {
		return new ReaderString();
	}

	public static IReader< UUID > uuid() {
		return new ReaderUUID();
	}
}
