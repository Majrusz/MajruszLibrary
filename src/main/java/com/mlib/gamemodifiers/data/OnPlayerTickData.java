package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;

@Deprecated
public class OnPlayerTickData extends ContextData.Event< TickEvent.PlayerTickEvent > {
	public final Player player;

	public OnPlayerTickData( TickEvent.PlayerTickEvent event ) {
		super( event.player, event );
		this.player = event.player;
	}
}
