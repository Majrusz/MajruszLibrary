package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class OnLootGenerated implements IEntityData {
	public final ObjectArrayList< ItemStack > generatedLoot;
	public final ResourceLocation lootId;
	public final LootContext context;
	public final BlockState blockState;
	public final DamageSource damageSource;
	public final Entity killer;
	public final Entity entity;
	public final Player lastDamagePlayer;
	public final ItemStack tool;
	public final Vec3 origin;

	public static Context< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
		return Contexts.get( OnLootGenerated.class ).add( consumer );
	}

	public OnLootGenerated( ObjectArrayList< ItemStack > generatedLoot, ResourceLocation lootId, LootContext context ) {
		this.generatedLoot = generatedLoot;
		this.lootId = lootId;
		this.context = context;
		this.blockState = this.getParameter( LootContextParams.BLOCK_STATE );
		this.damageSource = this.getParameter( LootContextParams.DAMAGE_SOURCE );
		this.killer = this.getParameter( LootContextParams.KILLER_ENTITY );
		this.entity = this.getParameter( LootContextParams.THIS_ENTITY );
		this.lastDamagePlayer = this.getParameter( LootContextParams.LAST_DAMAGE_PLAYER );
		this.tool = this.getParameter( LootContextParams.TOOL );
		this.origin = this.getParameter( LootContextParams.ORIGIN );
	}

	@Override
	public Entity getEntity() {
		return this.entity != null ? this.entity : this.killer;
	}

	@Override
	public Level getLevel() {
		return this.entity != null ? this.entity.level() : null;
	}

	@Override
	public Vec3 getPosition() {
		return this.origin != null ? this.origin : new Vec3( 0.0, 0.0, 0.0 );
	}

	public boolean hasBlockState() {
		return this.blockState != null;
	}

	public boolean hasDamageSource() {
		return this.damageSource != null;
	}

	public boolean hasKiller() {
		return this.killer != null;
	}

	public boolean hasEntity() {
		return this.entity != null;
	}

	public boolean hasLastDamagePlayer() {
		return this.lastDamagePlayer != null;
	}

	public boolean hasTool() {
		return this.tool != null;
	}

	public boolean hasOrigin() {
		return this.origin != null;
	}

	private < Type > Type getParameter( LootContextParam< Type > parameter ) {
		return this.context.getParamOrNull( parameter );
	}
}
