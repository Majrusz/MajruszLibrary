package com.majruszlibrary.mixininterfaces;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public interface IMixinEntity {
	void majruszlibrary$addGlowTicks( int ticks );

	void majruszlibrary$addInvisibleTicks( int ticks );

	int majruszlibrary$getInvisibleTicks();

	@Nullable CompoundTag majruszlibrary$getExtraTag();

	CompoundTag majruszlibrary$getOrCreateExtraTag();
}
