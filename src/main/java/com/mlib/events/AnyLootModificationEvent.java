package com.mlib.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

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
	public final Vec3 origin;

	public AnyLootModificationEvent( List< ItemStack > generatedLoot, @Nullable BlockState blockState, @Nullable DamageSource damageSource,
		@Nullable Entity killer, @Nullable Entity entity, @Nullable ItemStack tool, @Nullable Vec3 origin
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
