package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class OnDamaged implements IEntityData {
	public final DamageSource source;
	public final LivingEntity attacker;
	public final LivingEntity target;
	public final float damage;

	public static Context< OnDamaged > listen( Consumer< OnDamaged > consumer ) {
		return Contexts.get( OnDamaged.class ).add( consumer );
	}

	public OnDamaged( DamageSource source, LivingEntity target, float damage ) {
		this.source = source;
		this.attacker = this.source.getEntity() instanceof LivingEntity entity ? entity : null;
		this.target = target;
		this.damage = damage;
	}

	@Override
	public Entity getEntity() {
		return this.target;
	}

	public boolean isDirect() {
		return this.source.getDirectEntity() == this.attacker;
	}
}
