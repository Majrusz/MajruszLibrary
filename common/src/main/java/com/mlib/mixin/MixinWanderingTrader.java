package com.mlib.mixin;

import com.mlib.contexts.OnWanderingTradesUpdated;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( WanderingTrader.class )
public abstract class MixinWanderingTrader {
	@Inject(
		at = @At( "TAIL" ),
		method = "updateTrades ()V"
	)
	private void updateTrades( CallbackInfo callback ) {
		Contexts.dispatch( new OnWanderingTradesUpdated( ( ( WanderingTrader )( Object )this ) ) );
	}
}
