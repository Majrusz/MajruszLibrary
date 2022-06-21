package com.mlib.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class DeferredRegisterHelper {
	private final String modId;
	private final List< DeferredRegister< ? > > registers = new ArrayList<>();

	public DeferredRegisterHelper( String modId ) {
		this.modId = modId;
	}

	public < Type > DeferredRegister< Type > create( ResourceKey< Registry< Type > > key ) {
		DeferredRegister< Type > register = DeferredRegister.create( key, this.modId );
		this.registers.add( register );

		return register;
	}

	public void registerAll() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		this.registers.forEach( register->register.register( modEventBus ) );
	}

	public ResourceLocation getLocation( String register ) {
		return new ResourceLocation( this.modId, register );
	}
}
