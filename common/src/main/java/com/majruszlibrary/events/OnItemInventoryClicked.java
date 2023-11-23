package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ICancellableEvent;
import com.majruszlibrary.platform.Side;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@OnlyIn( Dist.CLIENT )
public class OnItemInventoryClicked implements ICancellableEvent {
	public final @Nullable Slot slot;
	public final ItemStack itemStack;
	public final LocalPlayer player;
	public final int action;
	public final int containerIdx;
	private boolean isCancelled = false;

	public static Event< OnItemInventoryClicked > listen( Consumer< OnItemInventoryClicked > consumer ) {
		return Events.get( OnItemInventoryClicked.class ).add( consumer );
	}

	public OnItemInventoryClicked( Slot slot, int action ) {
		this.slot = slot;
		this.itemStack = slot != null ? slot.getItem() : ItemStack.EMPTY;
		this.player = Side.getLocalPlayer();
		this.action = action;
		if( slot != null ) {
			this.containerIdx = this.itemStack.equals( this.player.containerMenu.getSlot( slot.index ).getItem() ) ? slot.index : slot.getContainerSlot();
		} else {
			this.containerIdx = -1;
		}
	}

	@Override
	public boolean isExecutionStopped() {
		return this.isCancelled();
	}

	public void cancel() {
		this.isCancelled = true;
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}
}
