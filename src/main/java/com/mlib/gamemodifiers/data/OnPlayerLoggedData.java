package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnPlayerLoggedData extends ContextData.Event< PlayerEvent.PlayerLoggedInEvent > {
	public final Player player;

	public OnPlayerLoggedData( PlayerEvent.PlayerLoggedInEvent event ) {
		super( event.getEntity(), event );
		this.player = event.getEntity();
	}
}
