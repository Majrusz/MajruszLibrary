package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnAnimalTamed implements IEntityEvent {
	public final Animal animal;
	public final Player tamer;

	public static Event< OnAnimalTamed > listen( Consumer< OnAnimalTamed > consumer ) {
		return Events.get( OnAnimalTamed.class ).add( consumer );
	}

	public OnAnimalTamed( Animal animal, Player player ) {
		this.animal = animal;
		this.tamer = player;
	}

	@Override
	public Entity getEntity() {
		return this.animal;
	}
}
