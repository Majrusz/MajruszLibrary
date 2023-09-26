package net.mlib.data;

import net.minecraft.world.entity.EntityType;

class ReaderEntityType extends ReaderStringCustom< EntityType< ? > > {
	@Override
	public EntityType< ? > convert( String id ) {
		return null; // TODO: Utility.getEntityType( id );
	}

	@Override
	public String convert( EntityType< ? > entityType ) {
		return null; // TODO: Utility.getRegistryString( entityType );
	}
}
