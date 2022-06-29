package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;

public class OnSpawnedData extends ContextData {
	public final LivingEntity target;
	public final boolean loadedFromDisk;

	public OnSpawnedData( LivingEntity target, boolean loadedFromDisk ) {
		super( target );
		this.target = target;
		this.loadedFromDisk = loadedFromDisk;
	}
}
