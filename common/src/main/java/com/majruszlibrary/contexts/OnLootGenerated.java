package com.majruszlibrary.contexts;

import com.majruszlibrary.contexts.base.Context;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.contexts.data.IEntityData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OnLootGenerated implements IEntityData {
	public final ObjectArrayList< ItemStack > generatedLoot;
	public final ResourceLocation lootId;
	public final LootContext context;
	public final ServerLevel level;
	public final @Nullable BlockState blockState;
	public final @Nullable DamageSource damageSource;
	public final @Nullable Entity killer;
	public final @Nullable Entity entity;
	public final @Nullable Player lastDamagePlayer;
	public final @Nullable ItemStack tool;
	public final @Nullable Vec3 origin;

	public static Context< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
		return Contexts.get( OnLootGenerated.class ).add( consumer );
	}

	public OnLootGenerated( ObjectArrayList< ItemStack > generatedLoot, @Nullable ResourceLocation lootId, LootContext context ) {
		this.generatedLoot = generatedLoot;
		this.lootId = lootId != null ? lootId : new ResourceLocation( "empty" );
		this.context = context;
		this.level = context.getLevel();
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
		return this.level;
	}

	@Override
	public Vec3 getPosition() {
		return this.origin != null ? this.origin : new Vec3( 0.0, 0.0, 0.0 );
	}

	private < Type > Type getParameter( LootContextParam< Type > parameter ) {
		return this.context.getParamOrNull( parameter );
	}
}
