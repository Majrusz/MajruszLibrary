package com.mlib.gamemodifiers;

public interface IRegistrable< RegistryType extends IRegistrable< RegistryType, ModifierType >, ModifierType extends GameModifier > {
	GameModifiersHolder< RegistryType, ModifierType > getHolder();
}
