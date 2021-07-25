package com.mlib.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

/** Certain useful functions for handling CompoundNBT. */
public class NBTHelper {
	/** Saves BlockPos in given nbt data. */
	public static void saveBlockPos( CompoundTag tag, String key, BlockPos position ) {
		tag.putInt( key + "X", position.getX() );
		tag.putInt( key + "Y", position.getY() );
		tag.putInt( key + "Z", position.getZ() );
	}

	/** Loads BlockPos from given nbt data. */
	public static BlockPos loadBlockPos( CompoundTag tag, String key ) {
		return new BlockPos( tag.getInt( key + "X" ), tag.getInt( key + "Y" ), tag.getInt( key + "Z" ) );
	}
}
