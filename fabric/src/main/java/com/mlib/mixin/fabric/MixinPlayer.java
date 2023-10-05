package com.mlib.mixin.fabric;

import com.mlib.contexts.OnBreakSpeedGet;
import com.mlib.contexts.base.Contexts;
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
	public void getDestroySpeed( BlockState blockState, CallbackInfoReturnable< Float > callback ) {
		Player player = ( Player )( Object )this;

		callback.setReturnValue( Contexts.dispatch( new OnBreakSpeedGet( player, blockState, callback.getReturnValue() ) ).value );
	}
}
