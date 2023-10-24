package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnItemDamaged implements ILevelData {
	public final @Nullable Player player;
	public final ItemStack itemStack;
	public final int original;
	public int damage;

	public static Context< OnItemDamaged > listen( Consumer< OnItemDamaged > consumer ) {
		return Contexts.get( OnItemDamaged.class ).add( consumer );
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
		return this.itemStack.getDamageValue() + this.damage >= this.itemStack.getMaxDamage();
	}
}
