package com.mlib.mixin;

import com.mlib.mixininterfaces.IMixinEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin( Entity.class )
public abstract class MixinEntity implements IMixinEntity {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "updateDynamicGameEventListener (Ljava/util/function/BiConsumer;)V", at = @At( "TAIL" ) )
	private void updateDynamicGameEventListener( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer, CallbackInfo callback ) {
		this.updateListeners( consumer );
	}
}
