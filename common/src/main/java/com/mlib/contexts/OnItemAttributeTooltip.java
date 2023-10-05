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
	public final ItemStack itemStack;
	public final Item item;
	public final Map< EquipmentSlot, List< Component > > components = new HashMap<>();

	public static Context< OnItemAttributeTooltip > listen( Consumer< OnItemAttributeTooltip > consumer ) {
		return Contexts.get( OnItemAttributeTooltip.class ).add( consumer );
	}

	public OnItemAttributeTooltip( ItemStack itemStack ) {
		this.itemStack = itemStack;
		this.item = itemStack.getItem();

		Stream.of( EquipmentSlot.values() ).forEach( slot->this.components.put( slot, new ArrayList<>() ) );
	}

	public void add( EquipmentSlot slot, Component component ) {
		this.components.get( slot ).add( component );
	}
}
