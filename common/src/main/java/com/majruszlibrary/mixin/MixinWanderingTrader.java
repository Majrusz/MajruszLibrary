package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnWanderingTradesUpdated;
import com.majruszlibrary.events.base.Events;
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
		Events.dispatch( new OnWanderingTradesUpdated( ( ( WanderingTrader )( Object )this ) ) );
	}
}
