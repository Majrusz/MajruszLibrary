package com.mlib.data;

import com.mlib.Utility;
import net.minecraft.world.entity.EntityType;

class ReaderEntityType extends ReaderStringCustom< EntityType< ? > > {
	@Override
	public EntityType< ? > convert( String id ) {
		return Utility.getEntityType( id );
	}

	@Override
	public String convert( EntityType< ? > entityType ) {
		return Utility.getRegistryString( entityType );
	}
}
