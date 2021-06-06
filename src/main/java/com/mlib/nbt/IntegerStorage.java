package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

/** Class representing Integer with easier writing and reading data. */
public class IntegerStorage extends BaseStorage< Integer > {
	public IntegerStorage( String key, Integer defaultValue ) {
		super( key, defaultValue );
	}

	@Override
	public CompoundNBT saveTo( CompoundNBT nbt ) {
		nbt.putInt( this.key, this.value );

		return nbt;
	}

	@Override
	public CompoundNBT loadFrom( CompoundNBT nbt ) {
		set( nbt.getInt( this.key ) );

		return nbt;
	}

	/** Increments stored value by 1. */
	public Integer increment() {
		return set( x->x+1 );
	}

	/** Decrements stored value by 1. */
	public Integer decrement() {
		return set( x->x-1 );
	}
}
