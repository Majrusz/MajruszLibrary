package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnEntityModelSetup;
import com.majruszlibrary.events.base.Events;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( HumanoidModel.class )
public abstract class MixinHumanoidModel {
	@Inject(
		at = @At(
			target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation (Lnet/minecraft/world/entity/LivingEntity;F)V",
			value = "INVOKE"
		),
		method = "setupAnim (Lnet/minecraft/world/entity/LivingEntity;FFFFF)V"
	)
	private void setupAnim( LivingEntity entity, float $$0, float $$1, float $$2, float $$3, float $$4, CallbackInfo callback ) {
		Events.dispatch( new OnEntityModelSetup( entity, ( HumanoidModel< ? > )( Object )this ) );
	}
}
