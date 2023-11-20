package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class OnExpOrbPickedUp implements IEntityData {
	public final Player player;
	public final ExperienceOrb orb;
	public final int original;
	public int experience;

	public static Context< OnExpOrbPickedUp > listen( Consumer< OnExpOrbPickedUp > consumer ) {
		return Contexts.get( OnExpOrbPickedUp.class ).add( consumer );
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
