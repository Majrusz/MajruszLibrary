package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;

@Deprecated
public class OnServerTickData extends ContextData.Event< TickEvent.ServerTickEvent > {
	public OnServerTickData( TickEvent.ServerTickEvent event ) {
		super( ( LivingEntity )null, event );
	}
}