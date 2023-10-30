package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.List;
import java.util.function.Consumer;

public class OnTradesInitialized {
	public final Int2ObjectMap< List< VillagerTrades.ItemListing > > trades;
	public final VillagerProfession profession;

	public static Context< OnTradesInitialized > listen( Consumer< OnTradesInitialized > consumer ) {
		return Contexts.get( OnTradesInitialized.class ).add( consumer );
	}

	public OnTradesInitialized( Int2ObjectMap< List< VillagerTrades.ItemListing > > trades, VillagerProfession profession ) {
		this.trades = trades;
		this.profession = profession;
	}

	public List< VillagerTrades.ItemListing > getTrades( int tier ) {
		return this.trades.get( tier );
	}
}
