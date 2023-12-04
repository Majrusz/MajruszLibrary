package com.majruszlibrary.events;

import com.majruszlibrary.annotation.Dist;
import com.majruszlibrary.annotation.OnlyIn;
import com.majruszlibrary.events.base.Event;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OnItemRenderColorsGet {
	public final ItemStack itemStack;
	private final Map< Integer, Integer > colors = new HashMap<>();

	public static Event< OnItemRenderColorsGet > listen( Consumer< OnItemRenderColorsGet > consumer ) {
		return Events.get( OnItemRenderColorsGet.class ).add( consumer );
	}

	public OnItemRenderColorsGet( ItemStack itemStack ) {
		this.itemStack = itemStack;
	}

	public void add( int layerIdx, int color ) {
		this.colors.put( layerIdx, color );
	}

	public boolean hasColorsDefined() {
		return this.colors.size() > 0;
	}

	@OnlyIn( Dist.CLIENT )
	public ItemColor toItemColor() {
		return new ItemColor( this.colors );
	}

	@OnlyIn( Dist.CLIENT )
	public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
		private final Map< Integer, Integer > colors;

		public ItemColor( Map< Integer, Integer > colors ) {
			this.colors = colors;
		}

		@Override
		public int getColor( ItemStack itemStack, int layerIdx ) {
			return this.colors.getOrDefault( layerIdx, 0xffffff );
		}
	}
}
