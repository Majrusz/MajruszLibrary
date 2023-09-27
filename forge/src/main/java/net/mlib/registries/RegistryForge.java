package net.mlib.registries;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

public class RegistryForge implements IRegistryPlatform {
	DeferredRegister< ? > deferredRegister = null;

	@Override
	public < Type > void register( RegistryGroup< Type > group ) {
		this.deferredRegister = DeferredRegister.create( group.registry.key(), group.helper.getModId() );
		this.deferredRegister.register( FMLJavaModLoadingContext.get().getModEventBus() );
	}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		net.minecraftforge.registries.RegistryObject< Type > forgeObject = ( ( DeferredRegister< Type > )this.deferredRegister ).register( object.id, object.newInstance );
		object.set( forgeObject );
	}
}
