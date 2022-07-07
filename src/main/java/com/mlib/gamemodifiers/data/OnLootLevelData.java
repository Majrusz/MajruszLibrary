package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnLootLevelData extends ContextData {
	public final LootingLevelEvent event;
	public final DamageSource source;

	public OnLootLevelData( LootingLevelEvent event ) {
		super( null );
		this.event = event;
		this.source = event.getDamageSource();
	}
}
