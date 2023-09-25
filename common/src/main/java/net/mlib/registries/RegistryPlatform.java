package net.mlib.registries;

public interface RegistryPlatform {
	< Type > void register( RegistryGroup< Type > group );

	< Type > void register( RegistryObject< Type > object );
}
