package net.mlib.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.mlib.registries.Registries;

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
