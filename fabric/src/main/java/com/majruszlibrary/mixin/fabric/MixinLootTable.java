package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.contexts.OnLootGenerated;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTable;
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
	ResourceLocation majruszlibrary$id = null;
	Consumer< ItemStack > majruszlibrary$consumer = itemStack->{};
	ObjectArrayList< ItemStack > majruszlibrary$items = new ObjectArrayList<>();

	@Override
	public void majruszlibrary$set( ResourceLocation id ) {
		this.majruszlibrary$id = id;
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private void getRandomItemsRaw( LootContext context, Consumer< ItemStack > consumer, CallbackInfo callback ) {
		Contexts.dispatch( new OnLootGenerated( this.majruszlibrary$items, this.majruszlibrary$id, context ) ).generatedLoot.forEach( this.majruszlibrary$consumer );
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
