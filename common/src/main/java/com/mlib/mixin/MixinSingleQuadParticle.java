package com.mlib.mixin;

import com.mlib.mixininterfaces.IMixinSingleQuadParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( SingleQuadParticle.class )
public abstract class MixinSingleQuadParticle {
	@ModifyVariable(
		at = @At( "STORE" ),
		method = "render (Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V",
		ordinal = 2
	)
	private float getY( float y ) {
		return this instanceof IMixinSingleQuadParticle particle ? particle.getY( y ) : y;
	}

	@ModifyVariable(
		at = @At( value = "STORE" ),
		method = "render (Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"
	)
	private Quaternionf getQuaternion( Quaternionf quaternion ) {
		return this instanceof IMixinSingleQuadParticle particle ? particle.getQuaternion( quaternion ) : quaternion;
	}
}
