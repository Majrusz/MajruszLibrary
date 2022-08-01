package com.mlib.events;

import com.mlib.loot_modifiers.LootHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
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
		this.blockState = LootHelper.getParameter( context, LootContextParams.BLOCK_STATE );
		this.damageSource = LootHelper.getParameter( context, LootContextParams.DAMAGE_SOURCE );
		this.killer = LootHelper.getParameter( context, LootContextParams.KILLER_ENTITY );
		this.entity = LootHelper.getParameter( context, LootContextParams.THIS_ENTITY );
		this.lastDamagePlayer = LootHelper.getParameter( context, LootContextParams.LAST_DAMAGE_PLAYER );
		this.tool = LootHelper.getParameter( context, LootContextParams.TOOL );
		this.origin = LootHelper.getParameter( context, LootContextParams.ORIGIN );
	}
}
