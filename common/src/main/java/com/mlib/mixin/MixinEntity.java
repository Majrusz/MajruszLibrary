package com.mlib.mixin;

import com.mlib.animations.IAnimableEntity;
import com.mlib.contexts.OnEntityNoiseCheck;
import com.mlib.contexts.OnEntityNoiseReceived;
import com.mlib.contexts.base.Contexts;
import com.mlib.entity.EntityNoiseListener;
import com.mlib.mixininterfaces.IMixinEntity;
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
	private VibrationSystem.User mlib$vibrationUser = null;
	private VibrationSystem.Data mlib$vibrationData = null;
	private DynamicGameEventListener< VibrationSystem.Listener > mlib$vibrationListener = null;
	private int mlib$glowTicks = 0;
	private int mlib$invisibleTicks = 0;

	@Inject(
		at = @At( "TAIL" ),
		method = "<init> (Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"
	)
	private void constructor( EntityType< ? > type, Level level, CallbackInfo callback ) {
		if( EntityNoiseListener.isSupported( ( ( Entity )( Object )this ).getClass() ) ) {
			this.mlib$vibrationUser = new Config( ( Entity )( Object )this );
			this.mlib$vibrationData = new VibrationSystem.Data();
			this.mlib$vibrationListener = new DynamicGameEventListener<>( new VibrationSystem.Listener( new VibrationSystem() {
				@Override
				public Data getVibrationData() {
					return MixinEntity.this.mlib$vibrationData;
				}

				@Override
				public User getVibrationUser() {
					return MixinEntity.this.mlib$vibrationUser;
				}
			} ) );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		this.mlib$glowTicks = Math.max( this.mlib$glowTicks - 1, 0 );
		this.mlib$invisibleTicks = Math.max( this.mlib$invisibleTicks - 1, 0 );
		if( this.mlib$vibrationData != null ) {
			VibrationSystem.Ticker.tick( this.level, this.mlib$vibrationData, this.mlib$vibrationUser );
		}
		if( this instanceof IAnimableEntity animable ) {
			animable.tickAnimations();
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "updateDynamicGameEventListener (Ljava/util/function/BiConsumer;)V"
	)
	private void updateDynamicGameEventListener( BiConsumer< DynamicGameEventListener< ? >, ServerLevel > consumer, CallbackInfo callback ) {
		if( this.mlib$vibrationListener != null && this.level instanceof ServerLevel level ) {
			consumer.accept( this.mlib$vibrationListener, level );
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isCurrentlyGlowing ()Z"
	)
	private void isCurrentlyGlowing( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.mlib$glowTicks > 0 );
	}

	@Inject(
		at = @At( "RETURN" ),
		cancellable = true,
		method = "isInvisible ()Z"
	)
	private void isInvisible( CallbackInfoReturnable< Boolean > callback ) {
		callback.setReturnValue( callback.getReturnValue() || this.mlib$invisibleTicks > 0 );
	}

	@Override
	public void mlib$addGlowTicks( int ticks ) {
		this.mlib$glowTicks += ticks;
	}

	@Override
	public void mlib$addInvisibleTicks( int ticks ) {
		this.mlib$invisibleTicks += ticks;
	}

	@Override
	public int mlib$getInvisibleTicks() {
		return this.mlib$invisibleTicks;
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
