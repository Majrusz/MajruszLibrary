package net.mlib.mixin.fabric;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.mlib.contexts.OnBreakSpeedGet;
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

		callback.setReturnValue( OnBreakSpeedGet.dispatch( player, blockState, callback.getReturnValue() ).newSpeed );
	}
}
