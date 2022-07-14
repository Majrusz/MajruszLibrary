package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import javax.annotation.Nullable;

public class OnDamagedData extends ContextData.Event< LivingHurtEvent > {
	public final DamageSource source;
	@Nullable public final LivingEntity attacker;
	public final LivingEntity target;

	public OnDamagedData( LivingHurtEvent event ) {
		super( event.getEntityLiving(), event );
		this.source = event.getSource();
		this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
		this.target = event.getEntityLiving();
	}
}