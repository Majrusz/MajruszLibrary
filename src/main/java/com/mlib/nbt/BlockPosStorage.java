package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class BlockPosStorage extends BaseStorage< BlockPos > {
	public BlockPosStorage( String key, BlockPos defaultValue ) {
		super( key, defaultValue );
	}

	public BlockPosStorage( String key, int x, int y, int z ) {
		this( key, new BlockPos( x, y, z ) );
	}

	@Override
	public void saveTo( CompoundNBT nbt ) {
		nbt.putInt( this.key + "X", this.value.getX() );
		nbt.putInt( this.key + "Y", this.value.getY() );
		nbt.putInt( this.key + "Z", this.value.getZ() );
	}

	@Override
	public void loadFrom( CompoundNBT nbt ) {
		this.value = new BlockPos( nbt.getInt( this.key + "X" ), nbt.getInt( this.key + "Y" ), nbt.getInt( this.key + "Z" ) );
	}
}
