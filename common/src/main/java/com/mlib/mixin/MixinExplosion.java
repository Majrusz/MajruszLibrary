package com.mlib.mixin;

import com.mlib.contexts.OnExploded;
import com.mlib.contexts.base.Contexts;
import com.mlib.mixininterfaces.IMixinExplosion;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin( Explosion.class )
public abstract class MixinExplosion implements IMixinExplosion {
	private @Shadow @Mutable boolean fire;
	private @Shadow @Mutable float radius;
	private @Shadow Level level;
	private @Shadow ObjectArrayList< BlockPos > toBlow;
	private OnExploded mlibContext = null;

	@Override
	public float getRadius() {
		return this.radius;
	}

	@Override
	public boolean getSpawnsFire() {
		return this.fire;
	}

	@Override
	public boolean isExplosionCancelled() {
		return this.mlibContext != null
			&& this.mlibContext.isExplosionCancelled();
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "explode ()V"
	)
	private void explode( CallbackInfo callback ) {
		this.mlibContext = Contexts.dispatch( new OnExploded( ( Explosion )( Object )this, this.level, this.radius, this.fire ) );
		if( this.mlibContext.isExplosionCancelled() ) {
			callback.cancel();
		} else {
			this.radius = this.mlibContext.radius;
			this.fire = this.mlibContext.spawnsFire;
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "finalizeExplosion (Z)V"
	)
	private void finalizeExplosion( boolean $$1, CallbackInfo callback ) {
		if( this.isExplosionCancelled() ) {
			callback.cancel();
		}
	}

	@Redirect(
		at = @At(
			target = "Lnet/minecraft/world/level/Level;getEntities (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
			value = "INVOKE"
		),
		method = "explode ()V"
	)
	private List< Entity > getEntities( Level level, Entity entity, AABB aabb ) {
		List< Entity > entities = level.getEntities( entity, aabb );
		this.mlibContext.filter( this.toBlow, entities );

		return entities;
	}
}
