package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.events.OnLootGenerated;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Consumer;

@Mixin( value = LootTable.class, priority = 990 )
public abstract class MixinLootTable implements IMixinLootTable {
	ResourceLocation majruszlibrary$id = null;
	Consumer< ItemStack > majruszlibrary$consumer = itemStack->{};
	ObjectArrayList< ItemStack > majruszlibrary$items = new ObjectArrayList<>();

	@Override
	public void majruszlibrary$set( ResourceLocation id ) {
		this.majruszlibrary$id = id;
	}

	@Override
	public void majruszlibrary$modify( LootContext context ) {
		Events.dispatch( new OnLootGenerated( this.majruszlibrary$items, this.majruszlibrary$id, context ) ).generatedLoot.forEach( this.majruszlibrary$consumer );
	}

	@ModifyVariable(
		at = @At( "HEAD" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private Consumer< ItemStack > getRandomItemsRaw( Consumer< ItemStack > consumer ) {
		this.majruszlibrary$consumer = consumer;
		this.majruszlibrary$items = new ObjectArrayList<>();

		return this.majruszlibrary$items::add;
	}
}
