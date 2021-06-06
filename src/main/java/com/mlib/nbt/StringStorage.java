package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

/** Class representing String with easier writing and reading data. */
public class StringStorage extends BaseStorage< String > {
	public StringStorage( String key, String defaultValue ) {
		super( key, defaultValue );
	}

	@Override
	public CompoundNBT saveTo( CompoundNBT nbt ) {
		nbt.putString( this.key, this.value );

		return nbt;
	}

	@Override
	public CompoundNBT loadFrom( CompoundNBT nbt ) {
		set( nbt.getString( this.key ) );

		return nbt;
	}
}
