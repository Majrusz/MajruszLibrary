package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.events.AnyLootModificationEvent;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;

public class OnLootData extends ContextData.Event< AnyLootModificationEvent > {
	public OnLootData( AnyLootModificationEvent event ) {
		super( Utility.castIfPossible( LivingEntity.class, event.entity ), event );
	}
}