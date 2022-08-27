package com.mlib.gamemodifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GameModifiersHolder< RegistryType extends IRegistrable > {
	final List< GameModifier > modifiers = new ArrayList<>();
	final String configKey;
	Supplier< RegistryType > lazySupplier;

	public GameModifiersHolder( String configKey, Supplier< RegistryType > lazySupplier ) {
		this.configKey = configKey;
		this.lazySupplier = ()->{
			RegistryType register = lazySupplier.get();
			register.setHolder( this );
			this.lazySupplier = ()->register;
			return register;
		};
	}

	public RegistryType getRegistry() {
		return this.lazySupplier.get();
	}

	public List< GameModifier > getModifiers() {
		return this.modifiers;
	}

	public < Type > void forEach( Class< Type > classType, Consumer< Type > consumer ) {
		for( GameModifier modifier : this.modifiers ) {
			if( classType.isInstance( modifier ) ) {
				consumer.accept( classType.cast( modifier ) );
			}
		}
	}

	public void addModifier( BiFunction< Supplier< RegistryType >, String, GameModifier > provider ) {
		this.modifiers.add( provider.apply( this::getRegistry, this.configKey ) );
	}
}
