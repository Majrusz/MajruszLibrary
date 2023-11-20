package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnEntityDamageBlocked implements IEntityData {
	public final DamageSource source;
	public final LivingEntity attacker;
	public final LivingEntity target;

	public static Context< OnEntityDamageBlocked > listen( Consumer< OnEntityDamageBlocked > consumer ) {
		return Contexts.get( OnEntityDamageBlocked.class ).add( consumer );
	}

	public OnEntityDamageBlocked( DamageSource source, LivingEntity target ) {
		this.source = source;
		this.attacker = this.source.getEntity() instanceof LivingEntity entity ? entity : null;
		this.target = target;
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public boolean isDirect() {
		return !this.source.isIndirect();
	}
}