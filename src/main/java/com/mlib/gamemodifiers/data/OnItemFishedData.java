package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

@Deprecated
public class OnItemFishedData extends ContextData.Event< ItemFishedEvent > {
	public final Player player;
	public final FishingHook hook;
	public final NonNullList< ItemStack > drops;

	public OnItemFishedData( ItemFishedEvent event ) {
		super( event.getPlayer(), event );
		this.player = event.getPlayer();
		this.hook = event.getHookEntity();
		this.drops = event.getDrops();
	}
}