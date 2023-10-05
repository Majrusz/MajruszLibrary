package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnBabySpawned implements IEntityData {
	public final Animal parentA;
	public final Animal parentB;
	public final Player player;
	public final AgeableMob child;

	public static Context< OnBabySpawned > listen( Consumer< OnBabySpawned > consumer ) {
		return Contexts.get( OnBabySpawned.class ).add( consumer );
	}

	public OnBabySpawned( Animal parentA, Animal parentB, Player player, AgeableMob child ) {
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
