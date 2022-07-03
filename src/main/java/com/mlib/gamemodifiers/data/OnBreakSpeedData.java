package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnBreakSpeedData extends ContextData {
	public final PlayerEvent.BreakSpeed event;
	public final Player player;

	public OnBreakSpeedData( PlayerEvent.BreakSpeed event ) {
		super( null );
		this.event = event;
		this.player = event.getPlayer();
	}
}
