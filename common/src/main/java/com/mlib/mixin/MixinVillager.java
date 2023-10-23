package com.mlib.mixin;

import com.mlib.contexts.OnTradesUpdated;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
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
		Contexts.dispatch( new OnTradesUpdated( player, ( ( Villager )( Object )this ).getOffers() ) );
	}
}
