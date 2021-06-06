package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

/** Class representing Boolean with easier writing and reading data. */
public class BooleanStorage extends BaseStorage< Boolean > {
	public BooleanStorage( String key, Boolean defaultValue ) {
		super( key, defaultValue );
	}

	@Override
	public CompoundNBT saveTo( CompoundNBT nbt ) {
		nbt.putBoolean( this.key, this.value );

		return nbt;
	}

	@Override
	public CompoundNBT loadFrom( CompoundNBT nbt ) {
		set( nbt.getBoolean( this.key ) );

		return nbt;
	}
}
