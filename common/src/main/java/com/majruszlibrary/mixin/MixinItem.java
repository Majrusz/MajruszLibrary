package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemCrafted;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Item.class )
public abstract class MixinItem {
	@Inject(
		at = @At( "HEAD" ),
		method = "onCraftedBy (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;)V"
	)
	private void onCraftedBy( ItemStack itemStack, Level level, Player player, CallbackInfo callback ) {
		Events.dispatch( new OnItemCrafted( player, itemStack ) );
	}
}
