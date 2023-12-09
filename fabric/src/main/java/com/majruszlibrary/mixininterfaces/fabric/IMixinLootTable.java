package com.majruszlibrary.mixininterfaces.fabric;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;

public interface IMixinLootTable {
	void majruszlibrary$set( ResourceLocation id );

	void majruszlibrary$modify( LootContext context );
}
