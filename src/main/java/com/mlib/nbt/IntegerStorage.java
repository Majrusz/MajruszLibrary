package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

public class IntegerStorage extends BaseStorage< Integer > {
	public IntegerStorage( String key, Integer defaultValue ) {
		super( key, defaultValue );
	}

	@Override
	public void saveTo( CompoundNBT nbt ) {
		nbt.putInt( this.key, this.value );
	}

	@Override
	public void loadFrom( CompoundNBT nbt ) {
		this.value = nbt.getInt( this.key );
	}
}
