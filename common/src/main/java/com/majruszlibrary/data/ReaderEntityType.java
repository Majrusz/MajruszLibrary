package com.majruszlibrary.data;

import com.majruszlibrary.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

class ReaderEntityType extends ReaderStringCustom< EntityType< ? > > {
	@Override
	public EntityType< ? > convert( String id ) {
		return Registries.getEntityType( new ResourceLocation( id ) );
	}

	@Override
	public String convert( EntityType< ? > entityType ) {
		return Registries.get( entityType ).toString();
	}
}
