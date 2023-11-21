package com.majruszlibrary.events;

import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import com.majruszlibrary.events.type.ILevelEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class OnItemBrewed implements ILevelEvent {
	public final Level level;
	public final BlockPos blockPos;
	public final List< ItemStack > items;

	public static Event< OnItemBrewed > listen( Consumer< OnItemBrewed > consumer ) {
		return Events.get( OnItemBrewed.class ).add( consumer );
	}

	public OnItemBrewed( Level level, BlockPos blockPos, List< ItemStack > items ) {
		this.level = level;
		this.blockPos = blockPos;
		this.items = items;
	}

	@Override
	public Level getLevel() {
		return this.level;
	}

	public void mapPotions( Function< List< ItemStack >, List< ItemStack > > mapper ) {
		List< ItemStack > potions = mapper.apply( this.items.subList( 0, 3 ) );
		for( int idx = 0; idx < 3; ++idx ) {
			this.items.set( idx, potions.get( idx ) );
		}
	}

	public void mapIngredient( Function< ItemStack, ItemStack > mapper ) {
		this.items.set( 3, mapper.apply( this.items.get( 3 ) ) );
	}

	public void mapFuel( Function< ItemStack, ItemStack > mapper ) {
		this.items.set( 4, mapper.apply( this.items.get( 4 ) ) );
	}
}
