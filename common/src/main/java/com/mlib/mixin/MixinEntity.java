package com.mlib.mixin;

import com.mlib.contexts.OnEntityNoiseCheck;
import com.mlib.contexts.OnEntityNoiseReceived;
import com.mlib.contexts.base.Contexts;
import com.mlib.entity.EntityNoiseListener;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin( Entity.class )
public abstract class MixinEntity {
	@Shadow private Level level;
	private VibrationSystem.User mlibVibrationUser = null;
	private VibrationSystem.Data mlibVibrationData = null;
	private DynamicGameEventListener< VibrationSystem.Listener > mlibVibrationListener = null;

	@Inject(
		at = @At( "TAIL" ),
		method = "<init> (Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"
	)
	private void constructor( EntityType< ? > type, Level level, CallbackInfo callback ) {
		if( EntityNoiseListener.isSupported( ( ( Entity )( Object )this ).getClass() ) ) {
			this.mlibVibrationUser = new Config( ( Entity )( Object )this );
			this.mlibVibrationData = new VibrationSystem.Data();
			this.mlibVibrationListener = new DynamicGameEventListener<>( new VibrationSystem.Listener( new VibrationSystem() {
				@Override
				public Data getVibrationData() {
					return MixinEntity.this.mlibVibrationData;
				}

				@Override
				public User getVibrationUser() {
					return MixinEntity.this.mlibVibrationUser;
				}
			} ) );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		if( this.mlibVibrationData != null ) {
			VibrationSystem.Ticker.tick( this.level, this.mlibVibrationData, this.mlibVibrationUser );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "updateDynamicGameEventListener (Ljava/util/function/BiConsumer;)V"
	)
	private void updateDynamicGameEventListener( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer, CallbackInfo callback ) {
		if( this.mlibVibrationListener != null && this.level instanceof ServerLevel level ) {
			consumer.accept( this.mlibVibrationListener, level );
		}
	}

	public static class Config implements VibrationSystem.User {
		final Entity entity;
		final PositionSource positionSource;

		Config( Entity entity ) {
			this.entity = entity;
			this.positionSource = new EntityPositionSource( entity, entity.getEyeHeight() );
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
			return Contexts.dispatch( new OnEntityNoiseCheck( level, position, this.entity ) ).isAudible();
		}

		@Override
		public void onReceiveVibration( ServerLevel level, BlockPos position, GameEvent event, Entity owner, Entity ownersProjectile, float distance ) {
			Contexts.dispatch( new OnEntityNoiseReceived( level, position, this.entity, owner, ownersProjectile, distance ) );
		}

		@Override
		public TagKey< GameEvent > getListenableEvents() {
			return GameEventTags.WARDEN_CAN_LISTEN;
		}
	}
}
