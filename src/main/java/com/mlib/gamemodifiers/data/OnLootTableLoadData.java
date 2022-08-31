package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.event.LootTableLoadEvent;

public class OnLootTableLoadData extends ContextData.Event< LootTableLoadEvent > {
	public final ResourceLocation name;
	public final LootTable table;
	public final LootTables lootTableManager;

	public OnLootTableLoadData( LootTableLoadEvent event ) {
		super( ( LivingEntity )null, event );
		this.name = event.getName();
		this.table = event.getTable();
		this.lootTableManager = event.getLootTableManager();
	}
}