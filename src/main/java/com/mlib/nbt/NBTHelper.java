package com.mlib.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

/** Certain useful functions for handling CompoundNBT. */
public class NBTHelper {
	/** Saves BlockPos in given nbt data. */
	public static void saveBlockPos( CompoundTag tag, String key, BlockPos position ) {
		tag.putInt( key + "X", position.getX() );
		tag.putInt( key + "Y", position.getY() );
		tag.putInt( key + "Z", position.getZ() );
	}

	/** Loads BlockPos from given nbt data. */
	public static BlockPos loadBlockPos( CompoundTag tag, String key ) {
		return new BlockPos( tag.getInt( key + "X" ), tag.getInt( key + "Y" ), tag.getInt( key + "Z" ) );
	}

	/** Sets integer value at given key and saves it automatically. */
	@Deprecated
	public static void setNBTInteger( LivingEntity entity, String key, int value ) {
		CompoundTag nbt = entity.getPersistentData();
		nbt.putInt( key, value );
	}

	/** Sets integer value at given key and saves it automatically. */
	@Deprecated
	public static void setNBTInteger( LivingEntity entity, String key, IValueFormula< Integer > formula ) {
		CompoundTag nbt = entity.getPersistentData();
		nbt.putInt( key, formula.apply( nbt.getInt( key ) ) );
	}

	/** Returns integer value at given key. */
	@Deprecated
	public static int getNBTInteger( LivingEntity entity, String key ) {
		CompoundTag nbt = entity.getPersistentData();

		return nbt.getInt( key );
	}

	/** Class for easier handling saving and storing integers. */
	public static class IntegerData {
		public final String key;
		public final CompoundTag nbt;

		public IntegerData( LivingEntity entity, String key ) {
			this.key = key;
			this.nbt = entity.getPersistentData();

			if( !this.nbt.contains( this.key ) )
				set( 0 );
		}

		/** Updates current integer value. */
		public void set( int value ) {
			this.nbt.putInt( this.key, value );
		}

		/** Updates current integer value using given formula. */
		public void set( IValueFormula< Integer > formula ) {
			this.nbt.putInt( this.key, formula.apply( get() ) );
		}

		/** Returns current saved integer value. */
		public int get() {
			return this.nbt.getInt( this.key );
		}
	}

	@FunctionalInterface
	public interface IValueFormula< Type > {
		Type apply( Type value );
	}
}
