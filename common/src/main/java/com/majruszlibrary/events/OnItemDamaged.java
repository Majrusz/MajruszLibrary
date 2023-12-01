package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ILevelEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnItemDamaged implements ILevelEvent {
	public final @Nullable Player player;
	public final ItemStack itemStack;
	public final int original;
	public int damage;

	public static Event< OnItemDamaged > listen( Consumer< OnItemDamaged > consumer ) {
		return Events.get( OnItemDamaged.class ).add( consumer );
	}

	public OnItemDamaged( @Nullable Player player, ItemStack itemStack, int damage ) {
		this.player = player;
		this.itemStack = itemStack;
		this.original = damage;
		this.damage = damage;
	}

	@Override
	public Level getLevel() {
		return this.player != null ? this.player.level() : null;
	}

	public int getExtraDamage() {
		return this.damage - this.original;
	}

	public boolean isAboutToBroke() {
		return this.itemStack.getDamageValue() + this.getExtraDamage() >= this.itemStack.getMaxDamage();
	}
}
