package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
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
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, String name, String comment ) {
			super( consumer, name, comment );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, "", "" );
		}

		public static void accept( Data data ) {
			CONTEXTS.accept( data );
		}
	}

	public static class Data extends ContextData {
		public final ItemStack itemStack;
		public final Item item;
		public final Map< EquipmentSlot, List< Component > > components = new HashMap<>();

		public Data( ItemStack itemStack ) {
			super( ( Entity )null );
			this.itemStack = itemStack;
			this.item = itemStack.getItem();
			Stream.of( EquipmentSlot.values() ).forEach( slot->this.components.put( slot, new ArrayList<>() ) );
		}

		public void add( EquipmentSlot slot, Component component ) {
			this.components.get( slot ).add( component );
		}
	}
}
