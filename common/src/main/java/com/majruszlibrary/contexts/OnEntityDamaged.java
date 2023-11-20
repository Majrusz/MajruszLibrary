package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnEntityDamaged implements IEntityData {
	public final DamageSource source;
	public final @Nullable LivingEntity attacker;
	public final LivingEntity target;
	public final float damage;

	public static Context< OnEntityDamaged > listen( Consumer< OnEntityDamaged > consumer ) {
		return Contexts.get( OnEntityDamaged.class ).add( consumer );
	}

	public OnEntityDamaged( DamageSource source, LivingEntity target, float damage ) {
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
		return !this.source.isIndirect();
	}
}
