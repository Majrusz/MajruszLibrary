package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

public class OnPlayerTickData extends ContextData {
	public final TickEvent.PlayerTickEvent event;
	public final Player player;

	public OnPlayerTickData( TickEvent.PlayerTickEvent event ) {
		super( null );
		this.event = event;
		this.player = event.player;
	}
}
