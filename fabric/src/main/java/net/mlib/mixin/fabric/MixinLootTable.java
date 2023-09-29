package net.mlib.mixin.fabric;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.mlib.contexts.OnLootGenerated;
import net.mlib.mixininterfaces.fabric.IMixinLootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

	@Shadow( aliases = { "this$0" } )
	@Inject(
		at = @At( "RETURN" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private void getRandomItemsRaw( LootContext context, Consumer< ItemStack > consumer, CallbackInfo callback ) {
		OnLootGenerated.dispatch( this.mlibItems, this.mlibId, context ).generatedLoot.forEach( this.mlibConsumer );
	}

	@Shadow( aliases = { "this$0" } )
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
