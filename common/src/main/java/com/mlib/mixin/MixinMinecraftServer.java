package com.mlib.mixin;

import com.mlib.contexts.OnServerTick;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin( MinecraftServer.class )
public abstract class MixinMinecraftServer {
	@Inject(
		at = @At( "TAIL" ),
		method = "tickServer (Ljava/util/function/BooleanSupplier;)V"
	)
	public void tickServer( BooleanSupplier haveTime, CallbackInfo callback ) {
		OnServerTick.dispatch();
	}
}
