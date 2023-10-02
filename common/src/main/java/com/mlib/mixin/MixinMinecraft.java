package com.mlib.mixin;

import com.mlib.contexts.OnClientTick;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Minecraft.class )
public abstract class MixinMinecraft {
	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	public void tick( CallbackInfo callback ) {
		OnClientTick.dispatch();
	}
}
