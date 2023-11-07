package com.mlib.mixin;

import com.mlib.contexts.OnExploded;
import com.mlib.contexts.base.Contexts;
import com.mlib.mixininterfaces.IMixinExplosion;
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
	private OnExploded mlib$context = null;

	@Override
	public float mlib$getRadius() {
		return this.radius;
	}

	@Override
	public boolean mlib$getSpawnsFire() {
		return this.fire;
	}

	@Override
	public boolean mlib$isExplosionCancelled() {
		return this.mlib$context != null
			&& this.mlib$context.isExplosionCancelled();
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "explode ()V"
	)
	private void explode( CallbackInfo callback ) {
		this.mlib$context = Contexts.dispatch( new OnExploded( ( Explosion )( Object )this, this.level, new Vec3( this.x, this.y, this.z ), this.radius, this.fire ) );
		if( this.mlib$context.isExplosionCancelled() ) {
			callback.cancel();
		} else {
			this.radius = this.mlib$context.radius;
			this.fire = this.mlib$context.spawnsFire;
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "finalizeExplosion (Z)V"
	)
	private void finalizeExplosion( boolean $$1, CallbackInfo callback ) {
		if( this.mlib$isExplosionCancelled() ) {
			callback.cancel();
		}
	}

	@ModifyVariable(
		at = @At( "STORE" ),
		method = "explode ()V"
	)
	private List< Entity > getEntities( List< Entity > entities ) {
		this.mlib$context.filter( this.toBlow, entities );

		return entities;
	}
}
