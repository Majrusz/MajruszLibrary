package net.mlib.registries;

import net.minecraft.core.Registry;
import net.mlib.annotations.PlatformImplementation;

@PlatformImplementation
public class RegistryPlatformImpl implements RegistryPlatform {
	@Override
	public < Type > void register( RegistryGroup< Type > group ) {}

	@Override
	public < Type > void register( RegistryObject< Type > object ) {
		Type value = object.newInstance.get();

		Registry.register( object.group.registry, object.group.helper.getLocation( object.id ), value );
		object.set( ()->value );
	}
}
