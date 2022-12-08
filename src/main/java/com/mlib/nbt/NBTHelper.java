package com.mlib.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class NBTHelper {
	public static void saveBlockPos( CompoundTag tag, String key, BlockPos position ) {
		tag.putInt( key + "X", position.getX() );
		tag.putInt( key + "Y", position.getY() );
		tag.putInt( key + "Z", position.getZ() );
	}

	public static BlockPos loadBlockPos( CompoundTag tag, String key ) {
		return new BlockPos( tag.getInt( key + "X" ), tag.getInt( key + "Y" ), tag.getInt( key + "Z" ) );
	}

	public static class IntegerData extends NBTData< Integer > {
		public IntegerData( LivingEntity entity, String key ) {
			super( entity, key, 0 );
		}

		public void set( Integer value ) {
			this.nbt.putInt( this.key, value );
		}

		public Integer get() {
			return this.nbt.getInt( this.key );
		}
	}

	public static class BooleanData extends NBTData< Boolean > {
		public BooleanData( LivingEntity entity, String key ) {
			super( entity, key, false );
		}

		public void set( Boolean value ) {
			this.nbt.putBoolean( this.key, value );
		}

		public Boolean get() {
			return this.nbt.getBoolean( this.key );
		}
	}

	public static class FloatData extends NBTData< Float > {
		public FloatData( LivingEntity entity, String key ) {
			super( entity, key, 0.0f );
		}

		public void set( Float value ) {
			this.nbt.putFloat( this.key, value );
		}

		public Float get() {
			return this.nbt.getFloat( this.key );
		}
	}

	public static abstract class NBTData< Type > {
		public final String key;
		public final CompoundTag nbt;

		public NBTData( LivingEntity entity, String key, Type defaultValue ) {
			this.key = key;
			this.nbt = entity.getPersistentData();
			if( !this.nbt.contains( this.key ) ) {
				this.set( defaultValue );
			}
		}

		public abstract void set( Type value );

		public void set( IValueFormula< Type > formula ) {
			this.set( formula.apply( get() ) );
		}

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
