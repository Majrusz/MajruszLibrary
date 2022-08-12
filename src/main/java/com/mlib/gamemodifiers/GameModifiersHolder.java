package com.mlib.gamemodifiers;

import com.mlib.config.ConfigGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class GameModifiersHolder< RegistryType > {
	final List< GameModifier > modifiers = new ArrayList<>();
	final ConfigGroup group;
	Supplier< RegistryType > lazySupplier;

	public GameModifiersHolder( ConfigGroup group, Supplier< RegistryType > lazySupplier ) {
		this.group = group;
		this.lazySupplier = ()->{
			RegistryType register = lazySupplier.get();
			this.lazySupplier = ()->register;
			return register;
		};
	}

	public RegistryType getRegistry() {
		return this.lazySupplier.get();
	}

	public void addModifier( Function< Supplier< RegistryType >, GameModifier > provider ) {
		this.modifiers.add( provider.apply( this::getRegistry ) );
	}
}
