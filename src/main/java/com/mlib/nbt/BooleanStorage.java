package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

/** Class representing Boolean with easier writing and reading data. */
public class BooleanStorage extends BaseStorage< Boolean > {
	public BooleanStorage( String key, Boolean defaultValue ) {
		super( key, defaultValue );
	}

	@Override
	public void saveTo( CompoundNBT nbt ) {
		nbt.putBoolean( this.key, this.value );
	}

	@Override
	public void loadFrom( CompoundNBT nbt ) {
		this.value = nbt.getBoolean( this.key );
	}
}
