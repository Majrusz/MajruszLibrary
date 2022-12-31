package com.mlib.mixin;

import com.mlib.MajruszLibrary;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin( Player.class )
public abstract class MixinPlayer extends MixinEntity {
	VibrationListener.VibrationListenerConfig mlibVibrationListenerConfig = new VibrationListener.VibrationListenerConfig() {
		@Override
		public TagKey< GameEvent > getListenableEvents() {
			return GameEventTags.WARDEN_CAN_LISTEN;
		}

		@Override
		public boolean shouldListen( ServerLevel p_223872_, GameEventListener p_223873_, BlockPos p_223874_, GameEvent p_223875_, GameEvent.Context p_223876_
		) {
			return true;
		}

		@Override
		public void onSignalReceive( ServerLevel p_223865_, GameEventListener p_223866_, BlockPos p_223867_, GameEvent p_223868_, @Nullable Entity p_223869_,
			@Nullable Entity p_223870_, float p_223871_
		) {
			MajruszLibrary.logOnDev( "dupa :D %s %f", p_223867_, p_223871_ );
		}
	};
	DynamicGameEventListener< VibrationListener > mlibVibrationListener;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "<init> (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLcom/mojang/authlib/GameProfile;)V", at = @At( "TAIL" ) )
	private void constructor( Level level, BlockPos position, float rot, GameProfile profile, CallbackInfo callback ) {
		Player player = ( Player )( Object )this;
		this.mlibVibrationListener = new DynamicGameEventListener<>( new VibrationListener( new EntityPositionSource( player, player.getEyeHeight() ), 16, this.mlibVibrationListenerConfig ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "tick ()V", at = @At( "TAIL" ) )
	private void tick( CallbackInfo callback ) {
		Player player = ( Player )( Object )this;
		if( !player.level.isClientSide ) {
			this.mlibVibrationListener.getListener().tick( player.level );
		}
	}

	@Override
	public void updateListeners( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer ) {
		Player player = ( Player )( Object )this;
		if( player.level instanceof ServerLevel level ) {
			consumer.accept( this.mlibVibrationListener, level );
		}
	}
}
