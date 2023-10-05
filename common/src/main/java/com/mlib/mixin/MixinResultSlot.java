package com.mlib.mixin;

import com.mlib.contexts.OnItemCrafted;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ResultSlot.class )
public abstract class MixinResultSlot {
	@Shadow private CraftingContainer craftSlots;
	@Shadow private Player player;

	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy (Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V",
			value = "INVOKE"
		),
		method = "checkTakeAchievements (Lnet/minecraft/world/item/ItemStack;)V"
	)
	private void checkTakeAchievements( ItemStack itemStack, CallbackInfo callback ) {
		Contexts.dispatch( new OnItemCrafted( this.player, itemStack, this.craftSlots ) );
	}
}
