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

	@Deprecated
	public static void setNBTInteger( LivingEntity entity, String key, int value ) {
		CompoundTag nbt = entity.getPersistentData();
		nbt.putInt( key, value );
	}

	@Deprecated
	public static void setNBTInteger( LivingEntity entity, String key, IValueFormula< Integer > formula ) {
		CompoundTag nbt = entity.getPersistentData();
		nbt.putInt( key, formula.apply( nbt.getInt( key ) ) );
	}

	@Deprecated
	public static int getNBTInteger( LivingEntity entity, String key ) {
		CompoundTag nbt = entity.getPersistentData();
		return nbt.getInt( key );
	}

	/** Class for easier handling, saving and storing integers. */
	public static class IntegerData extends NBTData< Integer > {
		public IntegerData( LivingEntity entity, String key ) {
			super( entity, key, 0 );
		}

		/** Updates stored integer value. */
		public void set( Integer value ) {
			this.nbt.putInt( this.key, value );
		}

		/** Returns stored integer value. */
		public Integer get() {
			return this.nbt.getInt( this.key );
		}
	}

	/** Class for easier handling, saving and storing booleans. */
	public static class BooleanData extends NBTData< Boolean > {
		public BooleanData( LivingEntity entity, String key ) {
			super( entity, key, false );
		}

		/** Updates stored boolean value. */
		public void set( Boolean value ) {
			this.nbt.putBoolean( this.key, value );
		}

		/** Returns stored boolean value. */
		public Boolean get() {
			return this.nbt.getBoolean( this.key );
		}
	}

	/** Class for easier handling, saving and storing floats. */
	public static class FloatData extends NBTData< Float > {
		public FloatData( LivingEntity entity, String key ) {
			super( entity, key, 0.0f );
		}

		/** Updates stored float value. */
		public void set( Float value ) {
			this.nbt.putFloat( this.key, value );
		}

		/** Returns stored float value. */
		public Float get() {
			return this.nbt.getFloat( this.key );
		}
	}

	/** Class for easier handling, saving and storing given data type. */
	public static abstract class NBTData< Type > {
		public final String key;
		public final CompoundTag nbt;

		public NBTData( LivingEntity entity, String key, Type defaultValue ) {
			this.key = key;
			this.nbt = entity.getPersistentData();
			if( !this.nbt.contains( this.key ) )
				set( defaultValue );
		}

		/** Updates stored value. */
		public abstract void set( Type value );

		/** Updates stored value using given formula. */
		public void set( IValueFormula< Type > formula ) {
			set( formula.apply( get() ) );
		}

		/** Returns stored value. */
		public abstract Type get();

		@FunctionalInterface
		public interface IValueFormula< Type > {
			Type apply( Type value );
		}
	}

	@Deprecated
	@FunctionalInterface
	public interface IValueFormula< Type > {
		Type apply( Type value );
	}
}
