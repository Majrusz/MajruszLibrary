package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Basic class storing primitive type with easier handling of writing and reading data. */
public abstract class BaseStorage< Type > {
	protected final String key;
	protected Type value;

	public BaseStorage( String key, Type defaultValue ) {
		this.key = key;
		set( defaultValue );
	}

	/** Saves information to CompoundNBT from given storages. */
	public static void saveTo( CompoundNBT nbt, BaseStorage< ? >... storages ) {
		for( BaseStorage< ? > storage : storages )
			storage.saveTo( nbt );
	}

	/** Loads information from CompoundNBT to given storages. */
	public static void loadFrom( CompoundNBT nbt, BaseStorage< ? >... storages ) {
		for( BaseStorage< ? > storage : storages )
			storage.loadFrom( nbt );
	}

	/** Saves information to CompoundNBT. */
	public abstract void saveTo( CompoundNBT nbt );

	/** Loads information from CompoundNBT. */
	public abstract void loadFrom( CompoundNBT nbt );

	/** Sets value. */
	public Type set( Type value ) {
		return this.value = value;
	}

	/** Sets value using function that takes current value as a parameter. */
	public Type set( Function< Type, Type > functionToCall ) {
		return set( functionToCall.apply( get() ) );
	}

	/** Returns value depending on type. */
	public Type get() {
		return this.value;
	}
}
