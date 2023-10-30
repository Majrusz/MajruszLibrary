package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnExplosion {
	public final Explosion explosion;
	public final @Nullable LivingEntity entity;
	public final float originalRadius;
	public float radius;
	public boolean spawnsFire;
	private Predicate< BlockPos > positionFilter = blockPos->false;
	private Predicate< Entity > entityFilter = entity->false;
	private boolean isExplosionCancelled = false;

	public static Context< OnExplosion > listen( Consumer< OnExplosion > consumer ) {
		return Contexts.get( OnExplosion.class ).add( consumer );
	}

	public OnExplosion( Explosion explosion, float radius, boolean spawnsFire ) {
		this.explosion = explosion;
		this.entity = explosion.getIndirectSourceEntity();
		this.originalRadius = radius;
		this.radius = radius;
		this.spawnsFire = spawnsFire;
	}

	public void filter( List< BlockPos > positions, List< Entity > entities ) {
		positions.removeIf( this.positionFilter );
		entities.removeIf( this.entityFilter );
	}

	public void skipBlockIf( Predicate< BlockPos > filter ) {
		this.positionFilter = this.positionFilter.or( filter );
	}

	public void skipEntityIf( Predicate< Entity > filter ) {
		this.entityFilter = this.entityFilter.or( filter );
	}

	public void cancelExplosion() {
		this.isExplosionCancelled = true;
	}

	public boolean isExplosionCancelled() {
		return this.isExplosionCancelled;
	}
}
