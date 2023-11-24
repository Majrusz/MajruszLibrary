package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ILevelEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class OnRaidDefeated implements ILevelEvent {
	public final Raid raid;
	public final List< Player > players = new ArrayList<>();

	public static Event< OnRaidDefeated > listen( Consumer< OnRaidDefeated > consumer ) {
		return Events.get( OnRaidDefeated.class ).add( consumer );
	}

	public OnRaidDefeated( Raid raid, Set< UUID > uuids ) {
		this.raid = raid;
		for( UUID uuid : uuids ) {
			Player player = raid.getLevel().getPlayerByUUID( uuid );
			if( player != null ) {
				this.players.add( player );
			}
		}
	}

	@Override
	public Level getLevel() {
		return this.raid.getLevel();
	}
}
