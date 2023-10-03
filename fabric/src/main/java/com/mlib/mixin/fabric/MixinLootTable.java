package com.mlib.mixin.fabric;

import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Contexts;
import com.mlib.mixininterfaces.fabric.IMixinLootTable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin( LootTable.class )
public abstract class MixinLootTable implements IMixinLootTable {
	ResourceLocation mlibId = null;
	Consumer< ItemStack > mlibConsumer = itemStack->{};
	ObjectArrayList< ItemStack > mlibItems = new ObjectArrayList<>();

	@Override
	public void set( ResourceLocation id ) {
		this.mlibId = id;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private void getRandomItemsRaw( LootContext context, Consumer< ItemStack > consumer, CallbackInfo callback ) {
		Contexts.dispatch( new OnLootGenerated( this.mlibItems, this.mlibId, context ) ).generatedLoot.forEach( this.mlibConsumer );
	}

	@ModifyVariable(
		at = @At( "HEAD" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private Consumer< ItemStack > getRandomItemsRaw( Consumer< ItemStack > consumer ) {
		this.mlibConsumer = consumer;
		this.mlibItems = new ObjectArrayList<>();

		return this.mlibItems::add;
	}
}
