package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.IEntityEvent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnBabySpawned implements IEntityEvent {
	public final Animal parentA;
	public final Animal parentB;
	public final AgeableMob child;
	public final @Nullable Player player;

	public static Event< OnBabySpawned > listen( Consumer< OnBabySpawned > consumer ) {
		return Events.get( OnBabySpawned.class ).add( consumer );
	}

	public OnBabySpawned( Animal parentA, Animal parentB, AgeableMob child, @Nullable Player player ) {
		this.parentA = parentA;
		this.parentB = parentB;
		this.player = player;
		this.child = child;
	}

	@Override
	public Entity getEntity() {
		return this.child;
	}
}
