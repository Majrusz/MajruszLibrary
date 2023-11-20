package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.contexts.OnBreakSpeedGet;
import com.majruszlibrary.contexts.base.Contexts;
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

		callback.setReturnValue( Contexts.dispatch( new OnBreakSpeedGet( player, blockState, callback.getReturnValue() ) ).speed );
	}
}
