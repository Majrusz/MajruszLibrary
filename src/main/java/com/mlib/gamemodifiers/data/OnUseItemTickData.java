package com.mlib.gamemodifiers.data;

import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

@Deprecated
public class OnUseItemTickData extends ContextData.Event< LivingEntityUseItemEvent.Tick > {
	public final ItemStack itemStack;
	public final int duration;

	public OnUseItemTickData( LivingEntityUseItemEvent.Tick event ) {
		super( event.getEntity(), event );
		this.itemStack = event.getItem();
		this.duration = event.getDuration();
	}
}
