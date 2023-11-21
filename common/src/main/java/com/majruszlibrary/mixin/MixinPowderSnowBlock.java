package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnEntityPowderSnowCheck;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( PowderSnowBlock.class )
public abstract class MixinPowderSnowBlock {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "canEntityWalkOnPowderSnow (Lnet/minecraft/world/entity/Entity;)Z"
	)
	private static void canEntityWalkOnPowderSnow( Entity entity, CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( Events.dispatch( new OnEntityPowderSnowCheck( entity, callback.getReturnValue() ) ).canWalk() );
	}
}
