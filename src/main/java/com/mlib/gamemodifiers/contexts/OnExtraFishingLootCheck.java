package com.mlib.gamemodifiers.contexts;

import com.mlib.Random;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.IEntityData;
import com.mlib.items.ItemHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ToolActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OnExtraFishingLootCheck {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( List< ItemStack > drops, Player player ) {
		return Contexts.get( Data.class ).dispatch( new Data( drops, player ) );
	}

	public static class Data implements IEntityData {
		public final List< ItemStack > drops;
		public final List< ItemStack > extraLoot = new ArrayList<>();
		public final Player player;
		public final ItemStack fishingRod;
		public int extraExperience = 0;
		public int extraRodDamage = 0;

		public Data( List< ItemStack > drops, Player player ) {
			this.drops = Collections.unmodifiableList( drops );
			this.player = player;
			this.fishingRod = ItemHelper.getMatchingHandItem( player, Data::isFishingRod );
		}

		public LootContext generateLootContext() {
			return new LootContext.Builder( this.getServerLevel() )
				.withParameter( LootContextParams.TOOL, this.fishingRod )
				.withRandom( Random.getThreadSafe() )
				.withLuck( this.player.getLuck() )
				.withParameter( LootContextParams.ORIGIN, this.player.position() )
				.create( LootContextParamSets.FISHING );
		}

		public boolean isExtraLootEmpty() {
			return this.extraLoot.isEmpty();
		}

		private static boolean isFishingRod( ItemStack itemStack ) {
			return itemStack.getItem().canPerformAction( itemStack, ToolActions.FISHING_ROD_CAST );
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}