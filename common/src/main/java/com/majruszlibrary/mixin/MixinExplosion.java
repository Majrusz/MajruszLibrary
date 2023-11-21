package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnExploded;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.mixininterfaces.IMixinExplosion;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin( Explosion.class )
public abstract class MixinExplosion implements IMixinExplosion {
	private @Shadow @Mutable boolean fire;
	private @Shadow @Mutable float radius;
	private @Shadow Level level;
	private @Shadow ObjectArrayList< BlockPos > toBlow;
	private @Shadow double x;
	private @Shadow double y;
	private @Shadow double z;
	private OnExploded majruszlibrary$event = null;

	@Override
	public float majruszlibrary$getRadius() {
		return this.radius;
	}

	@Override
	public boolean majruszlibrary$getSpawnsFire() {
		return this.fire;
	}

	@Override
	public boolean majruszlibrary$isExplosionCancelled() {
		return this.majruszlibrary$event != null
			&& this.majruszlibrary$event.isExplosionCancelled();
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "explode ()V"
	)
	private void explode( CallbackInfo callback ) {
		this.majruszlibrary$event = Events.dispatch( new OnExploded( ( Explosion )( Object )this, this.level, new Vec3( this.x, this.y, this.z ), this.radius, this.fire ) );
		if( this.majruszlibrary$event.isExplosionCancelled() ) {
			callback.cancel();
		} else {
			this.radius = this.majruszlibrary$event.radius;
			this.fire = this.majruszlibrary$event.spawnsFire;
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "finalizeExplosion (Z)V"
	)
	private void finalizeExplosion( boolean $$1, CallbackInfo callback ) {
		if( this.majruszlibrary$isExplosionCancelled() ) {
			callback.cancel();
		}
	}

	@ModifyVariable(
		at = @At( "STORE" ),
		method = "explode ()V"
	)
	private List< Entity > getEntities( List< Entity > entities ) {
		this.majruszlibrary$event.filter( this.toBlow, entities );

		return entities;
	}
}
