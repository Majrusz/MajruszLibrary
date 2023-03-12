package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.OnContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

// TODO: merge with OnLoot
public class OnAnyLootModification extends OnContext {
	private OnAnyLootModification() {}

	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return getContexts( Data.class ).add( consumer );
	}

	public static void dispatch( Data data ) {
		getContexts( Data.class ).dispatch( data );
	}

	public static class Data extends ContextData {
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
			super( context.getParam( LootContextParams.THIS_ENTITY ) );

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
}
