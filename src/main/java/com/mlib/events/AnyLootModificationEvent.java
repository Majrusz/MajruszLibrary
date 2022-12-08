package com.mlib.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import javax.annotation.Nullable;
import java.util.List;

/** Event called when any loot is generated. */
public class AnyLootModificationEvent extends Event implements IModBusEvent {
	public final List< ItemStack > generatedLoot;
	public final LootContext context;
	@Nullable public final BlockState blockState;
	@Nullable public final DamageSource damageSource;
	@Nullable public final Entity killer;
	@Nullable public final Entity entity;
	@Nullable public final Player lastDamagePlayer;
	@Nullable public final ItemStack tool;
	@Nullable public final Vec3 origin;

	public AnyLootModificationEvent( List< ItemStack > generatedLoot, LootContext context ) {
		this.generatedLoot = generatedLoot;
		this.context = context;
		this.blockState = this.getParameter( LootContextParams.BLOCK_STATE );
		this.damageSource = this.getParameter( LootContextParams.DAMAGE_SOURCE );
		this.killer = this.getParameter( LootContextParams.KILLER_ENTITY );
		this.entity = this.getParameter( LootContextParams.THIS_ENTITY );
		this.lastDamagePlayer = this.getParameter( LootContextParams.LAST_DAMAGE_PLAYER );
		this.tool = this.getParameter( LootContextParams.TOOL );
		this.origin = this.getParameter( LootContextParams.ORIGIN );
	}

	public < Type > Type getParameter( LootContextParam< Type > parameter ) {
		return this.context.hasParam( parameter ) ? this.context.getParam( parameter ) : null;
	}
}
