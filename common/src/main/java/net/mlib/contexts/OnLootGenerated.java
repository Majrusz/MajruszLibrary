package net.mlib.contexts;

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
import net.mlib.contexts.base.Condition;
import net.mlib.contexts.base.Context;
import net.mlib.contexts.base.Contexts;
import net.mlib.contexts.data.ILevelData;
import net.mlib.contexts.data.IPositionData;

import java.util.function.Consumer;

public class OnLootGenerated {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ObjectArrayList< ItemStack > generatedLoot, ResourceLocation lootId, LootContext context ) {
		return Contexts.get( Data.class ).dispatch( new Data( generatedLoot, lootId, context ) );
	}

	public static Condition< Data > hasBlockState() {
		return new Condition<>( data->data.blockState != null );
	}

	public static Condition< Data > hasDamageSource() {
		return new Condition<>( data->data.damageSource != null );
	}

	public static Condition< Data > hasKiller() {
		return new Condition<>( data->data.killer != null );
	}

	public static Condition< Data > hasEntity() {
		return new Condition<>( data->data.entity != null );
	}

	public static Condition< Data > hasLastDamagePlayer() {
		return new Condition<>( data->data.lastDamagePlayer != null );
	}

	public static Condition< Data > hasTool() {
		return new Condition<>( data->data.tool != null );
	}

	public static Condition< Data > hasOrigin() {
		return new Condition<>( data->data.origin != null );
	}

	public static class Data implements ILevelData, IPositionData {
		public final ObjectArrayList< ItemStack > generatedLoot;
		public final LootContext context;
		public final ResourceLocation lootId;
		public final BlockState blockState;
		public final DamageSource damageSource;
		public final Entity killer;
		public final Entity entity;
		public final Player lastDamagePlayer;
		public final ItemStack tool;
		public final Vec3 origin;

		public Data( ObjectArrayList< ItemStack > generatedLoot, ResourceLocation lootId, LootContext context ) {
			this.generatedLoot = generatedLoot;
			this.context = context;
			this.lootId = lootId;
			this.blockState = this.getParameter( LootContextParams.BLOCK_STATE );
			this.damageSource = this.getParameter( LootContextParams.DAMAGE_SOURCE );
			this.killer = this.getParameter( LootContextParams.KILLER_ENTITY );
			this.entity = this.getParameter( LootContextParams.THIS_ENTITY );
			this.lastDamagePlayer = this.getParameter( LootContextParams.LAST_DAMAGE_PLAYER );
			this.tool = this.getParameter( LootContextParams.TOOL );
			this.origin = this.getParameter( LootContextParams.ORIGIN );
		}

		@Override
		public Level getLevel() {
			return this.entity != null ? this.entity.level() : null;
		}

		@Override
		public Vec3 getPosition() {
			return this.origin != null ? this.origin : new Vec3( 0.0, 0.0, 0.0 );
		}

		private < Type > Type getParameter( LootContextParam< Type > parameter ) {
			return this.context.getParamOrNull( parameter );
		}
	}
}
