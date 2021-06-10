package com.mlib.nbt;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

/** Certain useful functions for handling CompoundNBT. */
public class NBTHelper {
	/** Saves BlockPos in given nbt data. */
	public static void saveBlockPos( CompoundNBT nbt, String key, BlockPos position ) {
		nbt.putInt( key + "X", position.getX() );
		nbt.putInt( key + "Y", position.getY() );
		nbt.putInt( key + "Z", position.getZ() );
	}

	/** Loads BlockPos from given nbt data. */
	public static BlockPos loadBlockPos( CompoundNBT nbt, String key ) {
		return new BlockPos( nbt.getInt( key + "X" ), nbt.getInt( key + "Y" ), nbt.getInt( key + "Z" ) );
	}
}
