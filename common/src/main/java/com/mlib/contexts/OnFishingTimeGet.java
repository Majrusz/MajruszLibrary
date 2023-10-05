package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

import java.util.function.Consumer;

public class OnFishingTimeGet implements IEntityData {
	public final FishingHook hook;
	public final Player player;
	public final int original;
	public int value;

	public static Context< OnFishingTimeGet > listen( Consumer< OnFishingTimeGet > consumer ) {
		return Contexts.get( OnFishingTimeGet.class ).add( consumer );
	}

	public OnFishingTimeGet( FishingHook hook, int value ) {
		this.hook = hook;
		this.player = hook.getPlayerOwner();
		this.original = value;
		this.value = value;
	}

	@Override
	public Entity getEntity() {
		return this.hook;
	}

	public int getTicks() {
		return Math.max( this.value, 1 );
	}
}
