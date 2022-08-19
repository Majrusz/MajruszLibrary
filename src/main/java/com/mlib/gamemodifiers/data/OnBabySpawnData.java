package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;

public class OnBabySpawnData extends ContextData.Event< BabyEntitySpawnEvent > {
	public final AgeableMob child;
	public final Mob parentA;
	public final Mob parentB;
	public final Player player;

	public OnBabySpawnData( BabyEntitySpawnEvent event ) {
		super( event.getChild(), event );
		this.child = event.getChild();
		this.parentA = event.getParentA();
		this.parentB = event.getParentB();
		this.player = event.getCausedByPlayer();
	}
}
