package com.mlib.gamemodifiers;

public interface IRegistrable< RegistryType extends IRegistrable< RegistryType > > {
	GameModifiersHolder< RegistryType > getHolder();
}
