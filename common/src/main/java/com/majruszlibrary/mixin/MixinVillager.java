package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnItemTraded;
import com.majruszlibrary.events.OnTradesUpdated;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Villager.class )
public abstract class MixinVillager {
	@Inject(
		at = @At( "TAIL" ),
		method = "updateSpecialPrices (Lnet/minecraft/world/entity/player/Player;)V"
	)
	private void updateSpecialPrices( Player player, CallbackInfo callback ) {
		Events.dispatch( new OnTradesUpdated( ( ( Villager )( Object )this ), player ) );
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "rewardTradeXp (Lnet/minecraft/world/item/trading/MerchantOffer;)V"
	)
	private void rewardTradeXp( MerchantOffer offer, CallbackInfo callback ) {
		Villager villager = ( ( Villager )( Object )this );

		Events.dispatch( new OnItemTraded( villager, offer ) );
	}
}
