package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

/** Class representing BlockPos with easier writing and reading data. */
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
		set( new BlockPos( nbt.getInt( this.key + "X" ), nbt.getInt( this.key + "Y" ), nbt.getInt( this.key + "Z" ) ) );
	}

	/** Returns x coordinate directly. */
	public int getX() {
		return get().getX();
	}

	/** Returns y coordinate directly. */
	public int getY() {
		return get().getY();
	}

	/** Returns z coordinate directly. */
	public int getZ() {
		return get().getZ();
	}
}
