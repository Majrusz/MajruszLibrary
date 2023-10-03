package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnAnimalTamed implements IEntityData {
	public final Animal animal;
	public final Player tamer;

	public static Context< OnAnimalTamed > listen( Consumer< OnAnimalTamed > consumer ) {
		return Contexts.get( OnAnimalTamed.class ).add( consumer );
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
