package com.mlib.mixin;

import com.mlib.contexts.OnItemBrushed;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( BrushableBlockEntity.class )
public abstract class MixinBrushableBlockEntity {
	@Shadow private ItemStack item;
	@Shadow private Direction hitDirection;
	@Shadow private ResourceLocation lootTable;
	private ResourceLocation mlibLocation;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "dropContent (Lnet/minecraft/world/entity/player/Player;)V", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split (I)Lnet/minecraft/world/item/ItemStack;" ) )
	private void dropContent( Player player, CallbackInfo callback ) {
		OnItemBrushed.dispatch( player, this.mlibLocation, this.item, this.hitDirection, ( BrushableBlockEntity )( Object )this );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "unpackLootTable (Lnet/minecraft/world/entity/player/Player;)V ", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;<init> (Lnet/minecraft/server/level/ServerLevel;)V" ) )
	private void unpackLootTable( Player player, CallbackInfo callback ) {
		this.mlibLocation = this.lootTable;
	}
}
