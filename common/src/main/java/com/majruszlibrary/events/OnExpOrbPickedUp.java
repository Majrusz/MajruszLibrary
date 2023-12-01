package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnExpOrbPickedUp implements IEntityEvent {
	public final Player player;
	public final ExperienceOrb orb;
	public final int original;
	public int experience;

	public static Event< OnExpOrbPickedUp > listen( Consumer< OnExpOrbPickedUp > consumer ) {
		return Events.get( OnExpOrbPickedUp.class ).add( consumer );
	}

	public OnExpOrbPickedUp( Player player, ExperienceOrb orb, int experience ) {
		this.player = player;
		this.orb = orb;
		this.original = experience;
		this.experience = experience;
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public int getExperience() {
		return this.experience;
	}
}
