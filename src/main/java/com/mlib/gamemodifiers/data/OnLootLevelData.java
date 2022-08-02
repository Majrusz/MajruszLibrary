package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

import javax.annotation.Nullable;

public class OnLootLevelData extends ContextData.Event< LootingLevelEvent > {
	@Nullable public final DamageSource source;

	public OnLootLevelData( LootingLevelEvent event ) {
		super( event.getDamageSource() != null ? Utility.castIfPossible( LivingEntity.class, event.getDamageSource().getEntity() ) : null, event );
		this.source = event.getDamageSource();
	}
}
