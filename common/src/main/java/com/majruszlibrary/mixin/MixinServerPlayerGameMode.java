package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnPlayerInteracted;
import com.majruszlibrary.events.base.Events;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ServerPlayerGameMode.class )
public abstract class MixinServerPlayerGameMode {
	@Inject(
		at = @At(
			ordinal = 0,
			target = "Lnet/minecraft/world/item/ItemStack;getCount ()I",
			value = "INVOKE"
		),
		cancellable = true,
		method = "useItem (Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"
	)
	private void useItem( ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand, CallbackInfoReturnable< InteractionResult > callback ) {
		OnPlayerInteracted data = Events.dispatch( new OnPlayerInteracted( player, hand ) );
		if( data.isInteractionCancelled() ) {
			callback.setReturnValue( data.getResult() );
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "useItemOn (Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
	)
	private void useItemOn( ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand, BlockHitResult result,
		CallbackInfoReturnable< InteractionResult > callback
	) {
		OnPlayerInteracted data = Events.dispatch( new OnPlayerInteracted( player, hand, result ) );
		if( data.isInteractionCancelled() ) {
			callback.setReturnValue( data.getResult() );
		}
	}
}
