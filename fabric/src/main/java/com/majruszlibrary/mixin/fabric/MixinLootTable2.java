package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin( value = LootTable.class, priority = 1010 )
public abstract class MixinLootTable2 implements IMixinLootTable {
	@Inject(
		at = @At( "RETURN" ),
		method = "getRandomItemsRaw (Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"
	)
	private void getRandomItemsRaw( LootContext context, Consumer< ItemStack > consumer, CallbackInfo callback ) {
		this.majruszlibrary$modify( context ); // compatibility with porting library
	}
}
