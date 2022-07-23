package com.mlib.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import javax.annotation.Nullable;

public class ProjectileShotEvent extends Event implements IModBusEvent {
	public final Projectile projectile;
	public final Level level;
	@Nullable final Entity owner;
	@Nullable public final ItemStack itemStack;
	public final CompoundTag customTag;

	public ProjectileShotEvent( Projectile projectile, @Nullable ItemStack itemStack, CompoundTag customTag ) {
		this.projectile = projectile;
		this.level = projectile.level;
		this.owner = projectile.getOwner();
		this.itemStack = itemStack;
		this.customTag = customTag;
	}
}
