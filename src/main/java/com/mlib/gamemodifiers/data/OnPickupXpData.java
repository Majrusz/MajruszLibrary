package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

public class OnPickupXpData extends ContextData.Event< PlayerXpEvent.PickupXp > {
	public final Player player;

	public OnPickupXpData( PlayerXpEvent.PickupXp event ) {
		super( event.getEntityLiving(), event );
		this.player = event.getPlayer();
	}
}
