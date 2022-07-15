package com.mlib.mixin;

import com.mlib.events.ItemSwingDurationEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( LivingEntity.class )
public abstract class MixinLivingEntity {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "getCurrentSwingDuration ()I", at = @At( "RETURN" ), cancellable = true )
	private void getCurrentSwingDuration( CallbackInfoReturnable< Integer > callback ) {
		LivingEntity livingEntity = ( LivingEntity )( Object )this;
		ItemSwingDurationEvent event = new ItemSwingDurationEvent( livingEntity, callback.getReturnValue() );
		MinecraftForge.EVENT_BUS.post( event );

		callback.setReturnValue( event.getTotalSwingDuration() );
	}
}
