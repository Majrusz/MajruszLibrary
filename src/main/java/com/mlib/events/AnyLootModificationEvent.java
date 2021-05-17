package com.mlib.events;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import javax.annotation.Nullable;
import java.util.List;

/** Event called when any loot is generated. */
public class AnyLootModificationEvent extends Event implements IModBusEvent {
	public final List< ItemStack > generatedLoot;
	@Nullable
	public final BlockState blockState;
	@Nullable
	public final DamageSource damageSource;
	@Nullable
	public final Entity killer;
	@Nullable
	public final Entity entity;
	@Nullable
	public final ItemStack tool;
	@Nullable
	public final Vector3d origin;

	public AnyLootModificationEvent( List< ItemStack > generatedLoot, @Nullable BlockState blockState, @Nullable DamageSource damageSource,
		@Nullable Entity killer, @Nullable Entity entity, @Nullable ItemStack tool, @Nullable Vector3d origin
	) {
		this.generatedLoot = generatedLoot;
		this.blockState = blockState;
		this.damageSource = damageSource;
		this.killer = killer;
		this.entity = entity;
		this.tool = tool;
		this.origin = origin;
	}
}
