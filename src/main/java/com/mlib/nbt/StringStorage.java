package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;

public class StringStorage extends BaseStorage< String > {
	public StringStorage( String key, String defaultValue ) {
		super( key, defaultValue );
	}

	@Override
	public void saveTo( CompoundNBT nbt ) {
		nbt.putString( this.key, this.value );
	}

	@Override
	public void loadFrom( CompoundNBT nbt ) {
		this.value = nbt.getString( this.key );
	}
}
