package com.mlib.mixin;

import com.mlib.events.ItemHurtEvent;
import net.minecraft.server.level.ServerPlayer;
import java.util.Random;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin( ItemStack.class )
public abstract class MixinItemStack {
	@Shadow( aliases = {"this$0"} )
	@Inject( method = "hurt(ILjava/util/Random;Lnet/minecraft/server/level/ServerPlayer;)Z", at = @At( "RETURN" ), cancellable = true )
	private void hurt( int damage, Random source, @Nullable ServerPlayer player, CallbackInfoReturnable< Boolean > callback ) {
		ItemStack itemStack = ( ItemStack )( Object )this;
		ItemHurtEvent itemHurtEvent = new ItemHurtEvent( player, itemStack, damage );
		MinecraftForge.EVENT_BUS.post( itemHurtEvent );
		if( itemHurtEvent.extraDamage != 0 ) {
			itemStack.setDamageValue( itemStack.getDamageValue() + itemHurtEvent.extraDamage );
		}

		callback.setReturnValue( itemHurtEvent.hasBeenBroken() );
	}
}
