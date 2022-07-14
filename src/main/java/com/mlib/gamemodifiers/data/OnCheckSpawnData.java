package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class OnCheckSpawnData extends ContextData.Event< LivingSpawnEvent.CheckSpawn > {
	public OnCheckSpawnData( LivingSpawnEvent.CheckSpawn event ) {
		super( event.getEntityLiving(), event );
	}
}