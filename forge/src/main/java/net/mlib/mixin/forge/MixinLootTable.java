package net.mlib.mixin.forge;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.mlib.contexts.OnLootGenerated;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LootTable.class )
public abstract class MixinLootTable {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getRandomItems (Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"
	)
	private void getRandomItems( LootContext context, CallbackInfoReturnable< ObjectArrayList< ItemStack > > callback ) {
		callback.setReturnValue( OnLootGenerated.dispatch( callback.getReturnValue(), ( ( LootTable )( Object )this ).getLootTableId(), context ).generatedLoot );
	}
}
