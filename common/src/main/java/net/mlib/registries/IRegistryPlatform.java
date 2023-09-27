package net.mlib.registries;

public interface IRegistryPlatform {
	< Type > void register( RegistryGroup< Type > group );

	< Type > void register( RegistryObject< Type > object );
}
