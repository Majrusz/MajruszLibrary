package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.events.AnyLootModificationEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnLoot {
	public static final Predicate< Data > HAS_BLOCK_STATE = data->data.blockState != null;
	public static final Predicate< Data > HAS_DAMAGE_SOURCE = data->data.damageSource != null;
	public static final Predicate< Data > HAS_KILLER = data->data.killer != null;
	public static final Predicate< Data > HAS_ENTITY = data->data.entity != null;
	public static final Predicate< Data > HAS_LAST_DAMAGE_PLAYER = data->data.lastDamagePlayer != null;
	public static final Predicate< Data > HAS_TOOL = data->data.tool != null;
	public static final Predicate< Data > HAS_ORIGIN = data->data.origin != null;

	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, String name, String comment ) {
			super( consumer, name, comment );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, "", "" );
		}

		@SubscribeEvent( priority = EventPriority.LOW )
		public static void onAnyLoot( AnyLootModificationEvent event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< AnyLootModificationEvent > {
		public final List< ItemStack > generatedLoot;
		public final LootContext context;
		@Nullable public final BlockState blockState;
		@Nullable public final DamageSource damageSource;
		@Nullable public final Entity killer;
		@Nullable public final Entity entity;
		@Nullable public final Player lastDamagePlayer;
		@Nullable public final ItemStack tool;
		@Nullable public final Vec3 origin;

		public Data( AnyLootModificationEvent event ) {
			super( Utility.castIfPossible( LivingEntity.class, event.entity ), event );
			this.generatedLoot = event.generatedLoot;
			this.context = event.context;
			this.blockState = event.blockState;
			this.damageSource = event.damageSource;
			this.killer = event.killer;
			this.entity = event.entity;
			this.lastDamagePlayer = event.lastDamagePlayer;
			this.tool = event.tool;
			this.origin = event.origin;
		}
	}
}
