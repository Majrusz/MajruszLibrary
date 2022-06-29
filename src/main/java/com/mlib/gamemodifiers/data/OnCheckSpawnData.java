package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import javax.annotation.Nullable;

public class OnCheckSpawnData extends ContextData {
	public final LivingSpawnEvent.CheckSpawn event;
	public final LivingEntity entity;
	@Nullable public final ServerLevel level;

	public OnCheckSpawnData( LivingSpawnEvent.CheckSpawn event ) {
		super( event.getEntityLiving() );
		this.event = event;
		this.entity = event.getEntityLiving();
		this.level = Utility.castIfPossible( ServerLevel.class, this.entity.level );
	}
}