package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

public class OnPlayerInteractData extends ContextData {
	public final PlayerInteractEvent event;
	public final ItemStack itemStack;
	public final Player player;
	@Nullable public final LivingEntity target;

	public OnPlayerInteractData( PlayerInteractEvent event, @Nullable LivingEntity target ) {
		super( event.getEntityLiving() );
		this.event = event;
		this.itemStack = event.getItemStack();
		this.player = event.getPlayer();
		this.target = target;
	}
}