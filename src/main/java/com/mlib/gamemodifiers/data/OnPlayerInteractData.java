package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

@Deprecated
public class OnPlayerInteractData extends ContextData.Event< PlayerInteractEvent > {
	public final ItemStack itemStack;
	public final Player player;
	@Nullable public final LivingEntity target;

	public OnPlayerInteractData( PlayerInteractEvent event, @Nullable LivingEntity target ) {
		super( event.getEntity(), event );
		this.itemStack = event.getItemStack();
		this.player = event.getEntity();
		this.target = target;
	}
}