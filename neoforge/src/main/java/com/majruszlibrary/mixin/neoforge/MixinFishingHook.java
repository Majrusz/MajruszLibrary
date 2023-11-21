package com.majruszlibrary.mixin.neoforge;

import com.majruszlibrary.events.OnItemFished;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin( FishingHook.class )
public abstract class MixinFishingHook {
	@Inject(
		at = @At(
			ordinal = 1,
			target = "Lnet/minecraft/advancements/critereon/FishingRodHookedTrigger;trigger (Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/projectile/FishingHook;Ljava/util/Collection;)V",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "retrieve (Lnet/minecraft/world/item/ItemStack;)I"
	)
	private void retrieve( ItemStack itemStack, CallbackInfoReturnable< Integer > callback, Player player, int damage, ItemFishedEvent event,
		LootParams lootParams, LootTable lootTable, List< ItemStack > items
	) {
		Events.dispatch( new OnItemFished( player, ( FishingHook )( Object )this, itemStack, items ) );
	}
}
