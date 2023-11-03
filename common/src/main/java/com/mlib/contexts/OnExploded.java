package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ICancellableData;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnExploded implements ICancellableData, ILevelData {
	public final Explosion explosion;
	public final Level level;
	public final Vec3 position;
	public final @Nullable LivingEntity entity;
	public final float originalRadius;
	public float radius;
	public boolean spawnsFire;
	private Predicate< BlockPos > positionFilter = blockPos->false;
	private Predicate< Entity > entityFilter = entity->false;
	private boolean isExplosionCancelled = false;

	public static Context< OnExploded > listen( Consumer< OnExploded > consumer ) {
		return Contexts.get( OnExploded.class ).add( consumer );
	}

	public OnExploded( Explosion explosion, Level level, Vec3 position, float radius, boolean spawnsFire ) {
		this.explosion = explosion;
		this.level = level;
		this.position = position;
		this.entity = explosion.getIndirectSourceEntity();
		this.originalRadius = radius;
		this.radius = radius;
		this.spawnsFire = spawnsFire;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isExplosionCancelled();
	}

	@Override
	public Level getLevel() {
		return this.level;
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
