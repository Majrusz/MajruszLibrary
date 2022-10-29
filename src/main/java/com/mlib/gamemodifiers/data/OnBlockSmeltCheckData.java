package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.events.BlockSmeltCheckEvent;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;

@Deprecated
public class OnBlockSmeltCheckData extends ContextData.Event< BlockSmeltCheckEvent > {
	public OnBlockSmeltCheckData( BlockSmeltCheckEvent event ) {
		super( Utility.castIfPossible( LivingEntity.class, event.player ), event );
	}
}