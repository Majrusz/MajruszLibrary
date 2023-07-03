package com.mlib.data;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ReaderResourceLocation extends ReaderStringCustom< ResourceLocation > {
	@Override
	public ResourceLocation convert( String id ) {
		return new ResourceLocation( id );
	}

	@Override
	public String convert( ResourceLocation location ) {
		return location.toString();
	}

	public interface Getter extends Supplier< ResourceLocation > {}

	public interface Setter extends Consumer< ResourceLocation > {}

	public interface ListGetter extends Supplier< List< ResourceLocation > > {}

	public interface ListSetter extends Consumer< List< ResourceLocation > > {}

	public interface MapGetter extends Supplier< Map< String, ResourceLocation > > {}

	public interface MapSetter extends Consumer< Map< String, ResourceLocation > > {}
}
