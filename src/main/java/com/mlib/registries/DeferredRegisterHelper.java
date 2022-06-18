package com.mlib.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DeferredRegisterHelper {
	private final String modId;
	private final List< Consumer< IEventBus > > consumers = new ArrayList<>();

	public DeferredRegisterHelper( String modId ) {
		this.modId = modId;
	}

	public < Type > void create( ResourceKey< Registry< Type > > key, Consumer< DeferredRegister< Type > > objectsToRegister ) {
		DeferredRegister< Type > register = DeferredRegister.create( key, this.modId );
		this.consumers.add( modEventBus -> {
			objectsToRegister.accept( register );
			register.register( modEventBus );
		} );
	}

	public void registerAll() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		this.consumers.forEach( consumer -> consumer.accept( modEventBus ) );
	}
}
