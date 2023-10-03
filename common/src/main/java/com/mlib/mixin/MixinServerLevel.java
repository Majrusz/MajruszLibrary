package com.mlib.mixin;

import com.mlib.contexts.OnBlockPlaced;
import com.mlib.contexts.base.Contexts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ServerLevel.class )
public abstract class MixinServerLevel {
	@Inject(
		at = @At( "RETURN" ),
		method = "sendBlockUpdated (Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;I)V"
	)
	public void sendBlockUpdated( BlockPos position, BlockState previousState, BlockState newState, int p_46615_, CallbackInfo callback ) {
		Contexts.dispatch( new OnBlockPlaced( ( ServerLevel )( Object )this, position, newState ) );
	}
}
