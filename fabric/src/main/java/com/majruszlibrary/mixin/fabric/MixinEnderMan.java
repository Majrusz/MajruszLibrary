package com.majruszlibrary.mixin.fabric;

import com.majruszlibrary.events.OnEnderManAngered;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( EnderMan.class )
public abstract class MixinEnderMan {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isLookingAtMe (Lnet/minecraft/world/entity/player/Player;)Z"
	)
	private void isLookingAtMe( Player player, CallbackInfoReturnable< Boolean > callback ) {
		if( callback.getReturnValue() && Events.dispatch( new OnEnderManAngered( ( EnderMan )( Object )this, player ) ).isAngerCancelled() ) {
			callback.setReturnValue( false );
		}
	}
}
