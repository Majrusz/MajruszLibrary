package com.mlib.mixin;

import com.mlib.gamemodifiers.contexts.OnEntitySignalCheck;
import com.mlib.gamemodifiers.contexts.OnEntitySignalReceived;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
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
	VibrationListener.VibrationListenerConfig mlibVibrationListenerConfig;
	DynamicGameEventListener< VibrationListener > mlibVibrationListener;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "<init> (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLcom/mojang/authlib/GameProfile;Lnet/minecraft/world/entity/player/ProfilePublicKey;)V", at = @At( "TAIL" ) )
	private void constructor( Level level, BlockPos position, float rot, GameProfile profile, @Nullable ProfilePublicKey publicKey, CallbackInfo callback ) {
		Player player = ( Player )( Object )this;
		this.mlibVibrationListenerConfig = new Config( player );
		this.mlibVibrationListener = new DynamicGameEventListener<>( new VibrationListener( new EntityPositionSource( player, player.getEyeHeight() ), 16, this.mlibVibrationListenerConfig, null, 0.0f, 0 ) );
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

	public static class Config implements VibrationListener.VibrationListenerConfig {
		final Player player;

		Config( Player player ) {
			this.player = player;
		}

		@Override
		public TagKey< GameEvent > getListenableEvents() {
			return GameEventTags.WARDEN_CAN_LISTEN;
		}

		@Override
		public boolean shouldListen( ServerLevel level, GameEventListener listener, BlockPos position, GameEvent event, GameEvent.Context eventContext ) {
			return OnEntitySignalCheck.dispatch( level, position, this.player ).shouldListen();
		}

		@Override
		public void onSignalReceive( ServerLevel level, GameEventListener listener, BlockPos position, GameEvent event, @Nullable Entity owner,
			@Nullable Entity ownersProjectile, float distance
		) {
			OnEntitySignalReceived.dispatch( level, position, this.player, owner, ownersProjectile, distance );
		}
	}
}
