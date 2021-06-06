package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

import java.util.function.Function;

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
	public abstract CompoundNBT saveTo( CompoundNBT nbt );

	/** Loads information from CompoundNBT. */
	public abstract CompoundNBT loadFrom( CompoundNBT nbt );

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

	/** Returns the key under which the value will be saved. */
	public String getKey() {
		return this.key;
	}

	/** Returns whether both of the objects are equal. */
	public boolean is( Type value ) {
		return value.equals( get() );
	}
}
