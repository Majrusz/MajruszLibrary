package com.majruszlibrary.mixin;

import com.majruszlibrary.animations.IAnimableEntity;
import com.majruszlibrary.events.OnEntityNoiseCheck;
import com.majruszlibrary.events.OnEntityNoiseReceived;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.entity.EntityNoiseListener;
import com.majruszlibrary.mixininterfaces.IMixinEntity;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin( Entity.class )
public abstract class MixinEntity implements IMixinEntity {
	private @Shadow Level level;
	private VibrationSystem.User majruszlibrary$vibrationUser = null;
	private VibrationSystem.Data majruszlibrary$vibrationData = null;
	private DynamicGameEventListener< VibrationSystem.Listener > majruszlibrary$vibrationListener = null;
	private int majruszlibrary$glowTicks = 0;
	private int majruszlibrary$invisibleTicks = 0;

	@Inject(
		at = @At( "TAIL" ),
		method = "<init> (Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"
	)
	private void constructor( EntityType< ? > type, Level level, CallbackInfo callback ) {
		if( EntityNoiseListener.isSupported( ( ( Entity )( Object )this ).getClass() ) ) {
			this.majruszlibrary$vibrationUser = new Config( ( Entity )( Object )this );
			this.majruszlibrary$vibrationData = new VibrationSystem.Data();
			this.majruszlibrary$vibrationListener = new DynamicGameEventListener<>( new VibrationSystem.Listener( new VibrationSystem() {
				@Override
				public Data getVibrationData() {
					return MixinEntity.this.majruszlibrary$vibrationData;
				}

				@Override
				public User getVibrationUser() {
					return MixinEntity.this.majruszlibrary$vibrationUser;
				}
			} ) );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		this.majruszlibrary$glowTicks = Math.max( this.majruszlibrary$glowTicks - 1, 0 );
		this.majruszlibrary$invisibleTicks = Math.max( this.majruszlibrary$invisibleTicks - 1, 0 );
		if( this.majruszlibrary$vibrationData != null ) {
			VibrationSystem.Ticker.tick( this.level, this.majruszlibrary$vibrationData, this.majruszlibrary$vibrationUser );
		}
		if( this instanceof IAnimableEntity animable ) {
			animable.getAnimations().tick();
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "updateDynamicGameEventListener (Ljava/util/function/BiConsumer;)V"
	)
	private void updateDynamicGameEventListener( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer, CallbackInfo callback ) {
		if( this.majruszlibrary$vibrationListener != null && this.level instanceof ServerLevel level ) {
			consumer.accept( this.majruszlibrary$vibrationListener, level );
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isCurrentlyGlowing ()Z"
	)
	private void isCurrentlyGlowing( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.majruszlibrary$glowTicks > 0 );
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isInvisible ()Z"
	)
	private void isInvisible( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.majruszlibrary$invisibleTicks > 0 );
	}

	@Override
	public void majruszlibrary$addGlowTicks( int ticks ) {
		this.majruszlibrary$glowTicks += ticks;
	}

	@Override
	public void majruszlibrary$addInvisibleTicks( int ticks ) {
		this.majruszlibrary$invisibleTicks += ticks;
	}

	@Override
	public int majruszlibrary$getInvisibleTicks() {
		return this.majruszlibrary$invisibleTicks;
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
			return Events.dispatch( new OnEntityNoiseCheck( level, position, this.entity ) ).isAudible();
		}

		@Override
		public void onReceiveVibration( ServerLevel level, BlockPos position, GameEvent event, Entity owner, Entity ownersProjectile, float distance ) {
			Events.dispatch( new OnEntityNoiseReceived( level, position, this.entity, owner, ownersProjectile, distance ) );
		}

		@Override
		public TagKey< GameEvent > getListenableEvents() {
			return GameEventTags.WARDEN_CAN_LISTEN;
		}
	}
}
