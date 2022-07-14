package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnBreakSpeedData extends ContextData.Event< PlayerEvent.BreakSpeed > {
	public final Player player;

	public OnBreakSpeedData( PlayerEvent.BreakSpeed event ) {
		super( event.getPlayer(), event );
		this.player = event.getPlayer();
	}
}
