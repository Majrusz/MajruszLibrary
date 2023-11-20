package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.ICancellableData;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntityDied implements ICancellableData, IEntityData {
	public final DamageSource source;
	public final @Nullable LivingEntity attacker;
	public final LivingEntity target;
	private boolean isDeathCancelled = false;

	public static Context< OnEntityDied > listen( Consumer< OnEntityDied > consumer ) {
		return Contexts.get( OnEntityDied.class ).add( consumer );
	}

	public OnEntityDied( DamageSource source, LivingEntity target ) {
		this.source = source;
		this.attacker = this.source.getEntity() instanceof LivingEntity entity ? entity : null;
		this.target = target;
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isDeathCancelled();
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public boolean isDirect() {
		return this.source.getDirectEntity() == this.attacker;
	}

	public void cancelDeath() {
		this.isDeathCancelled = true;
	}

	public boolean isDeathCancelled() {
		return this.isDeathCancelled;
	}
}
