package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class OnItemAttributeTooltip {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	public static Data dispatch( ItemStack itemStack ) {
		return Contexts.get( Data.class ).dispatch( new Data( itemStack ) );
	}

	public static class Data {
		public final ItemStack itemStack;
		public final Item item;
		public final Map< EquipmentSlot, List< Component > > components = new HashMap<>();

		public Data( ItemStack itemStack ) {
			this.itemStack = itemStack;
			this.item = itemStack.getItem();
			Stream.of( EquipmentSlot.values() ).forEach( slot->this.components.put( slot, new ArrayList<>() ) );
		}

		public void add( EquipmentSlot slot, Component component ) {
			this.components.get( slot ).add( component );
		}
	}
}
