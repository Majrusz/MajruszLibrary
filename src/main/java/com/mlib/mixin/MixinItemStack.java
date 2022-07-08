package com.mlib.mixin;

import com.mlib.MajruszLibrary;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin( ItemStack.class )
public abstract class MixinItemStack {
	@Inject( method = "hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z", at = @At( "RETURN" ), cancellable = true )
	private void hurt( int damage, RandomSource source, @Nullable ServerPlayer player, CallbackInfoReturnable< Boolean > callback ) {
		// MajruszLibrary.LOGGER.info( "" + damage + source + player );
		callback.setReturnValue( false );
	}
}