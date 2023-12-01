package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemBrushed;
import com.majruszlibrary.events.base.Events;
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
	private @Shadow ItemStack item;
	private @Shadow Direction hitDirection;
	private @Shadow ResourceLocation lootTable;
	private ResourceLocation majruszlibrary$location;

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/item/ItemStack;split (I)Lnet/minecraft/world/item/ItemStack;",
			value = "INVOKE"
		),
		method = "dropContent (Lnet/minecraft/world/entity/player/Player;)V"
	)
	private void dropContent( Player player, CallbackInfo callback ) {
		if( this.majruszlibrary$location == null ) {
			return;
		}

		Events.dispatch( new OnItemBrushed( player, this.majruszlibrary$location, this.item, this.hitDirection, ( BrushableBlockEntity )( Object )this ) );
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/world/level/storage/loot/LootParams$Builder;<init> (Lnet/minecraft/server/level/ServerLevel;)V",
			value = "INVOKE"
		),
		method = "unpackLootTable (Lnet/minecraft/world/entity/player/Player;)V "
	)
	private void unpackLootTable( Player player, CallbackInfo callback ) {
		this.majruszlibrary$location = this.lootTable;
	}
}
