package com.mlib.mixin;

import com.mlib.contexts.OnMobSpawnLimit;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( MobCategory.class )
public abstract class MixinMobCategory {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "getMaxInstancesPerChunk ()I", at = @At( "RETURN" ), cancellable = true )
	private void getMaxInstancesPerChunk( CallbackInfoReturnable< Integer > callback ) {
		callback.setReturnValue( OnMobSpawnLimit.dispatch( ( MobCategory )( Object )this, callback.getReturnValue() ).getSpawnLimit() );
	}
}
