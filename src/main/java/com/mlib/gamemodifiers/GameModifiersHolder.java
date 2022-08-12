package com.mlib.gamemodifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GameModifiersHolder< RegistryType extends IRegistrable< RegistryType, ModifierType >, ModifierType extends GameModifier > {
	final List< ModifierType > modifiers = new ArrayList<>();
	final String configKey;
	Supplier< RegistryType > lazySupplier;

	public GameModifiersHolder( String configKey, Supplier< RegistryType > lazySupplier ) {
		this.configKey = configKey;
		this.lazySupplier = ()->{
			RegistryType register = lazySupplier.get();
			this.lazySupplier = ()->register;
			return register;
		};
	}

	public RegistryType getRegistry() {
		return this.lazySupplier.get();
	}

	public List< ModifierType > getModifiers() {
		return this.modifiers;
	}

	public void addModifier( BiFunction< Supplier< RegistryType >, String, ModifierType > provider ) {
		this.modifiers.add( provider.apply( this::getRegistry, this.configKey ) );
	}
}
