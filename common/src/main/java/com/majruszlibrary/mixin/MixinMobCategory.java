package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnMobSpawnLimitGet;
import com.majruszlibrary.contexts.base.Contexts;
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
