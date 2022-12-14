package com.mlib.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomCreativeModeTab {
	protected final List< Consumer< CreativeModeTab.Builder > > consumers = new ArrayList<>();
	protected final List< ItemStack > itemStacks = new ArrayList<>();
	protected ResourceLocation location;

	public CustomCreativeModeTab( FMLJavaModLoadingContext context ) {
		context.getModEventBus().addListener( this::register );
	}

	public void add( ItemStack itemStack ) {
		this.itemStacks.add( itemStack );
	}

	public void add( Item item ) {
		this.add( new ItemStack( item ) );
	}

	public void apply( Consumer< CreativeModeTab.Builder > consumer ) {
		this.consumers.add( consumer );
	}

	public CustomCreativeModeTab location( ResourceLocation location ) {
		this.location = location;

		return this;
	}

	public CustomCreativeModeTab title( String title ) {
		this.apply( builder->builder.title( Component.literal( title ) ) );

		return this;
	}

	public CustomCreativeModeTab icon( Supplier< ItemStack > itemStack ) {
		this.apply( builder->builder.icon( itemStack ) );

		return this;
	}

	public CustomCreativeModeTab items( CreativeModeTab.DisplayItemsGenerator generator ) {
		this.apply( builder->builder.displayItems( generator ) );

		return this;
	}

	protected void defineItems( FeatureFlagSet flagSet, CreativeModeTab.Output output, boolean hasPermissions ) {
		output.acceptAll( this.itemStacks );
	}

	private void register( CreativeModeTabEvent.Register event ) {
		event.registerCreativeModeTab( this.location, builder->this.consumers.forEach( consumer->consumer.accept( builder ) ) );
	}
}
