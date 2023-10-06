package com.mlib.mixin;

import com.mlib.contexts.OnItemBrushed;
import com.mlib.contexts.base.Contexts;
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

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;split (I)Lnet/minecraft/world/item/ItemStack;",
			value = "INVOKE"
		),
		method = "dropContent (Lnet/minecraft/world/entity/player/Player;)V"
	)
	private void dropContent( Player player, CallbackInfo callback ) {
		Contexts.dispatch( new OnItemBrushed( player, this.mlibLocation, this.item, this.hitDirection, ( BrushableBlockEntity )( Object )this ) );
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;<init> (Lnet/minecraft/server/level/ServerLevel;)V",
			value = "INVOKE"
		),
		method = "unpackLootTable (Lnet/minecraft/world/entity/player/Player;)V "
	)
	private void unpackLootTable( Player player, CallbackInfo callback ) {
		this.mlibLocation = this.lootTable;
	}
}