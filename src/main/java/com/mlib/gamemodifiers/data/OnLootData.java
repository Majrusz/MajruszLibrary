package com.mlib.gamemodifiers.data;

import com.mlib.Utility;
import com.mlib.events.AnyLootModificationEvent;
import com.mlib.gamemodifiers.ContextData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class OnLootData extends ContextData.Event< AnyLootModificationEvent > {
	public final List< ItemStack > generatedLoot;
	@Nullable public final BlockState blockState;
	@Nullable public final DamageSource damageSource;
	@Nullable public final Entity killer;
	@Nullable public final Entity entity;
	@Nullable public final ItemStack tool;
	@Nullable public final Vec3 origin;

	public OnLootData( AnyLootModificationEvent event ) {
		super( Utility.castIfPossible( LivingEntity.class, event.entity ), event );
		this.generatedLoot = event.generatedLoot;
		this.blockState = event.blockState;
		this.damageSource = event.damageSource;
		this.killer = event.killer;
		this.entity = event.entity;
		this.tool = event.tool;
		this.origin = event.origin;
	}
}