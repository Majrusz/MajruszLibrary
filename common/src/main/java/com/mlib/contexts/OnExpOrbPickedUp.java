package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnExpOrbPickedUp {
	public final Player player;
	public final ExperienceOrb orb;
	public final int original;
	public int value;

	public static Context< OnExpOrbPickedUp > listen( Consumer< OnExpOrbPickedUp > consumer ) {
		return Contexts.get( OnExpOrbPickedUp.class ).add( consumer );
	}

	public OnExpOrbPickedUp( Player player, ExperienceOrb orb, int experience ) {
		this.player = player;
		this.orb = orb;
		this.original = experience;
		this.value = experience;
	}

	public int getExperience() {
		return this.value;
	}
}
