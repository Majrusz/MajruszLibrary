package com.mlib.gamemodifiers.data;

import com.mlib.ObfuscationGetter;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class OnLootTableCustomLoadData extends ContextData {
	static final ObfuscationGetter.Field< LootTable, List< LootPool > > POOLS = new ObfuscationGetter.Field<>( LootTable.class, "f_79109_" );
	static final ObfuscationGetter.Field< LootPool, LootPoolEntryContainer[] > ENTRIES = new ObfuscationGetter.Field<>( LootPool.class, "f_79023_" );
	public final ResourceLocation name;
	public final LootTable table;
	public final List< LootPool > pools;

	public OnLootTableCustomLoadData( ResourceLocation name, LootTable table ) {
		super( ( LivingEntity )null );
		this.name = name;
		this.table = table;
		this.pools = POOLS.get( table );
	}

	public void addEntry( int poolId, Item item, int weight, int quality, LootItemCondition.Builder... conditions ) {
		LootPool lootPool = this.pools.get( poolId );
		LootPoolEntryContainer[] entries = ENTRIES.get( lootPool );
		assert entries != null;
		LootPoolEntryContainer[] newEntries = new LootPoolEntryContainer[ entries.length + 1 ];
		System.arraycopy( entries, 0, newEntries, 0, entries.length );
		var builder = LootItem.lootTableItem( item ).setWeight( weight ).setQuality( quality );
		for( LootItemCondition.Builder condition : conditions ) {
			builder.when( condition );
		}
		newEntries[ entries.length ] = builder.build();
		ENTRIES.set( lootPool, newEntries );
	}

	public int addPool() {
		this.pools.add( LootPool.lootPool().name( String.format( "custom#%s%d", this.name.toString(), this.pools.size() ) ).build() );

		return this.pools.size() - 1;
	}
}