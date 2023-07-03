package com.mlib.data;

import com.mlib.Utility;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ReaderEntityType extends ReaderStringCustom< EntityType< ? > > {
	@Override
	public EntityType< ? > convert( String id ) {
		return Utility.getEntityType( id );
	}

	@Override
	public String convert( EntityType< ? > entityType ) {
		return Utility.getRegistryString( entityType );
	}

	public interface Getter extends Supplier< EntityType< ? > > {}

	public interface Setter extends Consumer< EntityType< ? > > {}

	public interface ListGetter extends Supplier< List< EntityType< ? > > > {}

	public interface ListSetter extends Consumer< List< EntityType< ? > > > {}

	public interface MapGetter extends Supplier< Map< String, EntityType< ? > > > {}

	public interface MapSetter extends Consumer< Map< String, EntityType< ? > > > {}
}
