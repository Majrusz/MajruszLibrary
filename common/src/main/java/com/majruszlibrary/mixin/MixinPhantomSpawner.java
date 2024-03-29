package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnInsomniaPhantomsCountGet;
import com.majruszlibrary.events.base.Events;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin( PhantomSpawner.class )
public abstract class MixinPhantomSpawner {
	private ServerPlayer majruszlibrary$lastPlayer;

	@ModifyVariable(
		at = @At( "STORE" ),
		method = "tick (Lnet/minecraft/server/level/ServerLevel;ZZ)I",
		name = { "l", "$$15" } // for some magic reason ordinal = 3 does not work on fabric
	)
	private int getPhantomsCount( int count ) {
		return Events.dispatch( new OnInsomniaPhantomsCountGet( this.majruszlibrary$lastPlayer, count ) ).getCount();
	}

	@Inject(
		at = @At(
			target = "Lnet/minecraft/server/level/ServerPlayer;blockPosition ()Lnet/minecraft/core/BlockPos;",
			value = "INVOKE"
		),
		locals = LocalCapture.CAPTURE_FAILHARD,
		method = "tick (Lnet/minecraft/server/level/ServerLevel;ZZ)I"
	)
	private void tick( ServerLevel level, boolean p_64577_, boolean p_64578_, CallbackInfoReturnable< Integer > callback, RandomSource $$0, int $$1,
		Iterator< ServerPlayer > iterator, ServerPlayer player
	) {
		this.majruszlibrary$lastPlayer = player;
	}
}
