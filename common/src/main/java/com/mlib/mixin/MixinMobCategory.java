package com.mlib.mixin;

import com.mlib.contexts.OnMobSpawnLimitGet;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( MobCategory.class )
public abstract class MixinMobCategory {
	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "getMaxInstancesPerChunk ()I"
	)
	private void getMaxInstancesPerChunk( CallbackInfoReturnable< Integer > callback ) {
		callback.setReturnValue( Contexts.dispatch( new OnMobSpawnLimitGet( ( MobCategory )( Object )this, callback.getReturnValue() ) ).getSpawnLimit() );
	}
}
