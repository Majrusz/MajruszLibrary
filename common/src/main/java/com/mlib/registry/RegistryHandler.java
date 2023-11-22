package com.mlib.registry;

import com.mlib.modhelper.ModHelper;
import net.minecraft.core.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RegistryHandler {
	final ModHelper helper;
	final List< RegistryGroup< ? > > groups = new ArrayList<>();
	final RegistryCallbacks callbacks = new RegistryCallbacks();

	public RegistryHandler( ModHelper helper ) {
		this.helper = helper;
	}

	public < Type > RegistryGroup< Type > create( Registry< Type > registry ) {
		RegistryGroup< Type > group = new RegistryGroup<>( this.helper, registry );
		this.groups.add( group );

		return group;
	}

	public < Type > void create( Class< Type > clazz, Consumer< Type > consumer ) {
		this.callbacks.add( clazz, consumer );
	}

	public void register() {
		this.groups.forEach( RegistryGroup::register );

		Registries.PLATFORM.register( this.callbacks );
	}
}
