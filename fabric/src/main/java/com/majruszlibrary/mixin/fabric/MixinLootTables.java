package com.majruszlibrary.mixin.fabric;

import com.google.common.collect.ImmutableMap;
import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( LootTables.class )
public abstract class MixinLootTables {
	@ModifyVariable(
		at = @At( "STORE" ),
		method = "apply (Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"
	)
	private ImmutableMap< ResourceLocation, LootTable > deserialize( ImmutableMap< ResourceLocation, LootTable > lootTables ) {
		lootTables.forEach( ( id, lootTable )->{
			if( lootTable instanceof IMixinLootTable mixinLootTable ) {
				mixinLootTable.majruszlibrary$set( id );
			}
		} );

		return lootTables;
	}
}
