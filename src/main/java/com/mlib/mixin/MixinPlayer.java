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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin( Player.class )
public abstract class MixinPlayer extends MixinEntity {
	VibrationSystem.User mlibVibrationUser;
	VibrationSystem.Data mlibVibrationData;
	DynamicGameEventListener< VibrationSystem.Listener > mlibVibrationListener;

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "<init> (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLcom/mojang/authlib/GameProfile;)V", at = @At( "TAIL" ) )
	private void constructor( Level level, BlockPos position, float rot, GameProfile profile, CallbackInfo callback ) {
		Player player = ( Player )( Object )this;
		this.mlibVibrationUser = new Config( player );
		this.mlibVibrationData = new VibrationSystem.Data();
		this.mlibVibrationListener = new DynamicGameEventListener<>( new VibrationSystem.Listener( new VibrationSystem() {
			@Override
			public Data getVibrationData() {
				return MixinPlayer.this.mlibVibrationData;
			}

			@Override
			public User getVibrationUser() {
				return MixinPlayer.this.mlibVibrationUser;
			}
		} ) );
	}

	@Shadow( aliases = { "this$0" } )
	@Inject( method = "tick ()V", at = @At( "TAIL" ) )
	private void tick( CallbackInfo callback ) {
		Player player = ( Player )( Object )this;
		if( player.level() instanceof ServerLevel level ) {
			VibrationSystem.Ticker.tick( level, this.mlibVibrationData, this.mlibVibrationUser );
		}
	}

	@Override
	public void updateListeners( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer ) {
		Player player = ( Player )( Object )this;
		if( player.level() instanceof ServerLevel level ) {
			consumer.accept( this.mlibVibrationListener, level );
		}
	}

	public static class Config implements VibrationSystem.User {
		final Player player;
		final PositionSource positionSource;

		Config( Player player ) {
			this.player = player;
			this.positionSource = new EntityPositionSource( player, player.getEyeHeight() );
		}

		@Override
		public int getListenerRadius() {
			return 16;
		}

		@Override
		public PositionSource getPositionSource() {
			return this.positionSource;
		}

		@Override
		public boolean canReceiveVibration( ServerLevel level, BlockPos position, GameEvent event, GameEvent.Context context ) {
			return OnEntitySignalCheck.dispatch( level, position, this.player ).shouldListen();
		}

		@Override
		public void onReceiveVibration( ServerLevel level, BlockPos position, GameEvent event, @Nullable Entity owner, @Nullable Entity ownersProjectile,
			float distance
		) {
			OnEntitySignalReceived.dispatch( level, position, this.player, owner, ownersProjectile, distance );
		}

		@Override
		public TagKey< GameEvent > getListenableEvents() {
			return GameEventTags.WARDEN_CAN_LISTEN;
		}
	}
}
