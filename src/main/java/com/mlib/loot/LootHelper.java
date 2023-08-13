package com.mlib.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.server.ServerLifecycleHooks;

public class LootHelper {
	public static LootTable getLootTable( ResourceLocation id ) {
		return ServerLifecycleHooks.getCurrentServer()
			.getLootData()
			.getLootTable( id );
	}

	public static LootParams toGiftParams( Entity entity ) {
		return new LootParams.Builder( ( ServerLevel )entity.level() )
			.withParameter( LootContextParams.ORIGIN, entity.position() )
			.withParameter( LootContextParams.THIS_ENTITY, entity )
			.create( LootContextParamSets.GIFT );
	}
}