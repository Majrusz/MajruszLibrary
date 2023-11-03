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
	ResourceLocation mlib$id = null;
	Consumer< ItemStack > mlib$consumer = itemStack->{};
	ObjectArrayList< ItemStack > mlib$items = new ObjectArrayList<>();

	@Override
	public void mlib$set( ResourceLocation id ) {
		this.mlib$id = id;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private void getRandomItemsRaw( LootContext context, Consumer< ItemStack > consumer, CallbackInfo callback ) {
		Contexts.dispatch( new OnLootGenerated( this.mlib$items, this.mlib$id, context ) ).generatedLoot.forEach( this.mlib$consumer );
	}

	@ModifyVariable(
		at = @At( "HEAD" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private Consumer< ItemStack > getRandomItemsRaw( Consumer< ItemStack > consumer ) {
		this.mlib$consumer = consumer;
		this.mlib$items = new ObjectArrayList<>();

		return this.mlib$items::add;
	}
}
