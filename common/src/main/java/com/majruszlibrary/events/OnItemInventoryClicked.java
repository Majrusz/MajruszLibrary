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
	public final int slotIdx;
	public final ItemStack itemStack;
	public final LocalPlayer player;
	public final int action;
	private boolean isCancelled = false;

	public static Event< OnItemInventoryClicked > listen( Consumer< OnItemInventoryClicked > consumer ) {
		return Events.get( OnItemInventoryClicked.class ).add( consumer );
	}

	public OnItemInventoryClicked( Slot slot, int action ) {
		this.slot = slot;
		this.slotIdx = slot != null ? slot.getContainerSlot() : -1;
		this.itemStack = slot != null ? slot.getItem() : ItemStack.EMPTY;
		this.player = Side.getLocalPlayer();
		this.action = action;
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
