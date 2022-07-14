package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnDimensionChangedData extends ContextData.Event< PlayerEvent.PlayerChangedDimensionEvent > {
	public final LivingEntity entity;
	public final ResourceKey< Level > from;
	public final ResourceKey< Level > to;

	public OnDimensionChangedData( PlayerEvent.PlayerChangedDimensionEvent event ) {
		super( event.getEntityLiving(), event );
		this.entity = event.getEntityLiving();
		this.from = event.getFrom();
		this.to = event.getTo();
	}
}