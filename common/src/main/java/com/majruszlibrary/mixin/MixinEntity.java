package com.majruszlibrary.mixin;

import com.majruszlibrary.animations.IAnimableEntity;
import com.majruszlibrary.entity.EntityNoiseListener;
import com.majruszlibrary.events.OnEntityNoiseCheck;
import com.majruszlibrary.events.OnEntityNoiseReceived;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.mixininterfaces.IMixinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin( Entity.class )
public abstract class MixinEntity implements IMixinEntity {
	private static final String majruszlibrary$TAG_ID = "MajruszLibrary";
	private @Shadow Level level;
	private VibrationListener.VibrationListenerConfig majruszlibrary$vibrationConfig = null;
	private DynamicGameEventListener< VibrationListener > majruszlibrary$vibrationListener = null;
	private int majruszlibrary$glowTicks = 0;
	private int majruszlibrary$invisibleTicks = 0;
	private CompoundTag majruszlibrary$extraTag = null;

	@Inject(
		at = @At( "TAIL" ),
		method = "<init> (Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"
	)
	private void constructor( EntityType< ? > type, Level level, CallbackInfo callback ) {
		Entity entity = ( Entity )( Object )this;
		if( EntityNoiseListener.isSupported( entity.getClass() ) ) {
			this.majruszlibrary$vibrationConfig = new Config( entity );
			this.majruszlibrary$vibrationListener = new DynamicGameEventListener<>( new VibrationListener( new EntityPositionSource( entity, entity.getEyeHeight() ), 16, this.majruszlibrary$vibrationConfig ) );
		}
	}

	@Inject(
		at = @At( "TAIL" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		this.majruszlibrary$glowTicks = Math.max( this.majruszlibrary$glowTicks - 1, 0 );
		this.majruszlibrary$invisibleTicks = Math.max( this.majruszlibrary$invisibleTicks - 1, 0 );
		if( this.majruszlibrary$vibrationConfig != null && !this.level.isClientSide ) {
			this.majruszlibrary$vibrationListener.getListener().tick( this.level );
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

	@Inject(
		at = @At( "RETURN" ),
		method = "saveWithoutId (Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"
	)
	private void saveWithoutId( CompoundTag tag, CallbackInfoReturnable< CompoundTag > callback ) {
		if( this.majruszlibrary$extraTag != null ) {
			tag.put( majruszlibrary$TAG_ID, this.majruszlibrary$extraTag );
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "load (Lnet/minecraft/nbt/CompoundTag;)V"
	)
	private void load( CompoundTag tag, CallbackInfo callback ) {
		if( tag.contains( majruszlibrary$TAG_ID ) ) {
			this.majruszlibrary$extraTag = tag.getCompound( majruszlibrary$TAG_ID );
		}
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

	public @Nullable CompoundTag majruszlibrary$getExtraTag() {
		return this.majruszlibrary$extraTag;
	}

	public CompoundTag majruszlibrary$getOrCreateExtraTag() {
		if( this.majruszlibrary$extraTag == null ) {
			this.majruszlibrary$extraTag = new CompoundTag();
		}

		return this.majruszlibrary$extraTag;
	}

	public static class Config implements VibrationListener.VibrationListenerConfig {
		final Entity entity;

		Config( Entity entity ) {
			this.entity = entity;
		}

		@Override
		public boolean shouldListen( ServerLevel level, GameEventListener listener, BlockPos position, GameEvent event, GameEvent.Context context ) {
			return Events.dispatch( new OnEntityNoiseCheck( level, position, this.entity ) ).isAudible();
		}

		@Override
		public void onSignalReceive( ServerLevel level, GameEventListener listener, BlockPos position, GameEvent event, @Nullable Entity owner,
			@Nullable Entity ownersProjectile, float distance
		) {
			Events.dispatch( new OnEntityNoiseReceived( level, position, this.entity, owner, ownersProjectile, distance ) );
		}

		@Override
		public TagKey< GameEvent > getListenableEvents() {
			return GameEventTags.WARDEN_CAN_LISTEN;
		}
	}
}
