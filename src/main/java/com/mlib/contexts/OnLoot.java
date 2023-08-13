package com.mlib.contexts;

import com.mlib.config.StringListConfig;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.base.Priority;
import com.mlib.contexts.data.ILevelData;
import com.mlib.contexts.data.IPositionData;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class OnLoot {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( List< ItemStack > generatedLoot, LootContext context ) {
		return Contexts.get( Data.class ).dispatch( new Data( generatedLoot, context ) );
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

	public static Condition< Data > is( StringListConfig ids ) {
		return new Condition< Data >( data->ids.contains( data.context.getQueriedLootTableId().toString() ) )
			.priority( Priority.HIGH )
			.configurable( true )
			.addConfig( ids );
	}

	public static Condition< Data > is( String... defaultIds ) {
		return is( DefaultConfigs.is( defaultIds ) );
	}

	public static Condition< Data > is( ResourceLocation... ids ) {
		return is( Stream.of( ids ).map( ResourceLocation::toString ).toArray( String[]::new ) );
	}

	public static class Data implements ILevelData, IPositionData {
		public final List< ItemStack > generatedLoot;
		public final LootContext context;
		@Nullable public final BlockState blockState;
		@Nullable public final DamageSource damageSource;
		@Nullable public final Entity killer;
		@Nullable public final Entity entity;
		@Nullable public final Player lastDamagePlayer;
		@Nullable public final ItemStack tool;
		@Nullable public final Vec3 origin;

		public Data( List< ItemStack > generatedLoot, LootContext context ) {
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

		@Override
		public Level getLevel() {
			return this.entity != null ? this.entity.getLevel() : null;
		}

		@Override
		public Vec3 getPosition() {
			return this.origin != null ? this.origin : new Vec3( 0.0, 0.0, 0.0 );
		}

		public void addAsChestLoot( ResourceLocation id ) {
			List< ItemStack > itemStacks = ServerLifecycleHooks.getCurrentServer()
				.getLootTables()
				.get( id )
				.getRandomItems( new LootContext.Builder( this.getServerLevel() )
					.withParameter( LootContextParams.ORIGIN, this.origin )
					.create( LootContextParamSets.CHEST )
				);

			this.generatedLoot.addAll( itemStacks );
		}

		@Nullable
		private < Type > Type getParameter( LootContextParam< Type > parameter ) {
			return this.context.getParamOrNull( parameter );
		}
	}

	public static class DefaultConfigs {
		public static StringListConfig is( String... defaultIds ) {
			StringListConfig ids = new StringListConfig( defaultIds );
			ids.name( "loot_table_ids" ).comment( "Determines to which loot tables it is applicable." );

			return ids;
		}
	}
}
