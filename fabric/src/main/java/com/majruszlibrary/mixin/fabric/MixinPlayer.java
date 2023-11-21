package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.events.OnBreakSpeedGet;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Player.class )
public abstract class MixinPlayer {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getDestroySpeed (Lnet/minecraft/world/level/block/state/BlockState;)F"
	)
	private void getDestroySpeed( BlockState blockState, CallbackInfoReturnable< Float > callback ) {
		Player player = ( Player )( Object )this;

		callback.setReturnValue( Events.dispatch( new OnBreakSpeedGet( player, blockState, callback.getReturnValue() ) ).speed );
	}
}
