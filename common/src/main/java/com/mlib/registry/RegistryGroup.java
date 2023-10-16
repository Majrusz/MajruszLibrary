package com.mlib.registry;

import com.mlib.modhelper.ModHelper;
import net.minecraft.core.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RegistryGroup< Type > {
	final ModHelper helper;
	final Registry< Type > registry;
	final List< RegistryObject< ? > > objects = new ArrayList<>();

	public RegistryGroup( ModHelper helper, Registry< Type > registry ) {
		this.helper = helper;
		this.registry = registry;
	}

	public < ObjectType extends Type > RegistryObject< ObjectType > create( String id, Supplier< ObjectType > supplier ) {
		RegistryObject< ObjectType > object = new RegistryObject<>( this, id, supplier );
		this.objects.add( object );

		return object;
	}

	void register() {
		Registries.PLATFORM.register( this );

		this.objects.forEach( RegistryObject::register );
	}
}
