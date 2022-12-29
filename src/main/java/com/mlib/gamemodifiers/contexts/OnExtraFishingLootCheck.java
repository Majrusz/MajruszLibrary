package com.mlib.gamemodifiers.contexts;

import com.mlib.Random;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.items.ItemHelper;
import net.minecraft.server.level.ServerLevel;
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
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public static Data accept( Data data ) {
			CONTEXTS.accept( data );

			return data;
		}

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}
	}

	public static class Data extends ContextData {
		public final List< ItemStack > drops;
		public final List< ItemStack > extraLoot = new ArrayList<>();
		public final Player player;
		public final ItemStack fishingRod;
		public int extraExperience = 0;
		public int extraRodDamage = 0;

		public Data( OnItemFished.Data data ) {
			super( data.player );

			this.drops = Collections.unmodifiableList( data.drops );
			this.player = data.player;
			this.fishingRod = ItemHelper.getMatchingHandItem( this.player, Data::isFishingRod );
		}

		public LootContext generateLootContext() {
			return new LootContext.Builder( ( ServerLevel )this.player.level )
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
	}
}