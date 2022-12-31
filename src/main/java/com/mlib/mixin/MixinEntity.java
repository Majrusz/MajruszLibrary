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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin( Entity.class )
public abstract class MixinEntity implements IMixinEntity {
	int mlibGlowTicks = 0;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "updateDynamicGameEventListener (Ljava/util/function/BiConsumer;)V", at = @At( "TAIL" ) )
	private void updateDynamicGameEventListener( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer, CallbackInfo callback ) {
		this.updateListeners( consumer );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "tick ()V", at = @At( "TAIL" ) )
	private void tick( CallbackInfo callback ) {
		this.mlibGlowTicks = Math.max( this.mlibGlowTicks - 1, 0 );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "isCurrentlyGlowing ()Z", at = @At( "RETURN" ), cancellable = true )
	private void isCurrentlyGlowing( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.mlibGlowTicks > 0 );
	}

	@Override
	public void addGlowTicks( int ticks ) {
		this.mlibGlowTicks += ticks;
	}
}
