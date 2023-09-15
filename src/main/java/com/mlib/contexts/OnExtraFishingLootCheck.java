package com.mlib.contexts;

import com.mlib.Random;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnExtraFishingLootCheck {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ItemFishedEvent event ) {
		return Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data extends OnItemFished.Data {
		public final List< ItemStack > extraLoot = new ArrayList<>();
		public int extraExperience = 0;
		public int extraRodDamage = 0;

		public Data( ItemFishedEvent event ) {
			super( event );
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
			return itemStack.getItem() instanceof FishingRodItem;
		}

		@Override
		public Entity getEntity() {
			return this.player;
		}
	}
}