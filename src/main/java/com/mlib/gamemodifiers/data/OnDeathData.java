package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nullable;

public class OnDeathData extends ContextData.Event< LivingDeathEvent > {
	public final DamageSource source;
	@Nullable public final LivingEntity attacker;
	public final LivingEntity target;

	public OnDeathData( LivingDeathEvent event ) {
		super( event.getEntity(), event );
		this.source = event.getSource();
		this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
		this.target = event.getEntity();
	}
}
