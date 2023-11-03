package com.mlib.contexts.base;

import com.mlib.contexts.data.ILevelData;
import com.mlib.contexts.data.IPositionData;
import com.mlib.level.LevelHelper;
import com.mlib.math.AnyPos;
import com.mlib.math.Random;
import com.mlib.platform.Side;
import com.mlib.time.TimeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Condition< DataType > {
	final Predicate< DataType > predicate;
	boolean isNegated = false;

	public static < DataType > Condition< DataType > isLogicalServer() {
		return new Condition<>( data->Side.isLogicalServer() );
	}

	public static < DataType extends ILevelData > Condition< DataType > hasLevel() {
		return new Condition<>( data->data.getLevel() != null );
	}

	public static < DataType > Condition< DataType > predicate( Predicate< DataType > predicate ) {
		return new Condition<>( predicate );
	}

	public static < DataType > Condition< DataType > predicate( Supplier< Boolean > check ) {
		return new Condition<>( data->check.get() );
	}

	public static < DataType > Condition< DataType > chance( Supplier< Float > chance ) {
		return new Condition<>( data->Random.check( chance.get() ) );
	}

	public static < DataType > Condition< DataType > chance( float chance ) {
		return Condition.chance( ()->chance );
	}

	public static < DataType extends ILevelData & IPositionData > Condition< DataType > chanceCRD( Supplier< Float > chance, Supplier< Boolean > scaledByCRD ) {
		return new Condition<>( data->{
			float totalChance = chance.get();
			if( scaledByCRD.get() && data.getLevel() != null ) {
				totalChance *= LevelHelper.getClampedRegionalDifficultyAt( data.getLevel(), AnyPos.from( data.getPosition() ).block() );
			}

			return Random.check( totalChance );
		} );
	}

	public static < DataType extends ILevelData & IPositionData > Condition< DataType > chanceCRD( float chance, boolean scaledByCRD ) {
		return Condition.chanceCRD( ()->chance, ()->scaledByCRD );
	}

	public static < DataType > Condition< DataType > cooldown( Supplier< Float > seconds ) {
		return new Condition<>( data->TimeHelper.haveSecondsPassed( seconds.get() ) );
	}

	public static < DataType > Condition< DataType > cooldown( float seconds ) {
		return Condition.cooldown( ()->seconds );
	}

	public static < DataType > Condition< DataType > isShiftKeyDown( Function< DataType, Player > player ) {
		return new Condition<>( data->player.apply( data ) != null && player.apply( data ).isShiftKeyDown() );
	}

	public static < DataType > Condition< DataType > isOnGround( Function< DataType, Entity > entity ) {
		return new Condition<>( data->entity.apply( data ) != null && entity.apply( data ).onGround() );
	}

	public static < DataType > Condition< DataType > isCooldownOver( Function< DataType, Player > player, Supplier< ? extends Item > item ) {
		return new Condition<>( data->player.apply( data ) != null && !player.apply( data ).getCooldowns().isOnCooldown( item.get() ) );
	}

	public static < DataType extends ILevelData > Condition< DataType > isLevel( List< ResourceLocation > levels ) {
		return new Condition<>( data->levels.contains( data.getLevel().dimension().location() ) );
	}

	public Condition< DataType > negate() {
		this.isNegated = !this.isNegated;

		return this;
	}

	public boolean isNegated() {
		return this.isNegated;
	}

	public boolean check( DataType data ) {
		return this.isNegated ^ this.predicate.test( data );
	}

	private Condition( Predicate< DataType > predicate ) {
		this.predicate = predicate;
	}
}
