package com.mlib.mixin;

import com.mlib.contexts.OnPlayerInteracted;
import com.mlib.contexts.base.Contexts;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( MultiPlayerGameMode.class )
public abstract class MixinMultiPlayerGameMode {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
			value = "INVOKE"
		),
		cancellable = true,
		method = "performUseItemOn (Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
	)
	private void performUseItemOn( LocalPlayer player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable< InteractionResult > callback ) {
		OnPlayerInteracted data = Contexts.dispatch( new OnPlayerInteracted( player, hand, result ) );
		if( data.hasResult() ) {
			callback.setReturnValue( data.getResult() );
		}
	}

	@Dynamic
	@Inject(
		at = @At( "RETURN" ),
		method = "*(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/player/Player;Lorg/apache/commons/lang3/mutable/MutableObject;I)Lnet/minecraft/network/protocol/Packet;"
	)
	private void useItem( InteractionHand hand, Player player, MutableObject< InteractionResult > result, int $$1,
		CallbackInfoReturnable< Packet< ServerGamePacketListener > > callback
	) {
		OnPlayerInteracted data = Contexts.dispatch( new OnPlayerInteracted( player, hand ) );
		if( data.hasResult() ) {
			result.setValue( data.getResult() );
		}
	}
}
