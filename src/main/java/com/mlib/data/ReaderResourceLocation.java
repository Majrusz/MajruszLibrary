package com.mlib.data;

import net.minecraft.resources.ResourceLocation;

class ReaderResourceLocation extends ReaderStringCustom< ResourceLocation > {
	@Override
	public ResourceLocation convert( String id ) {
		return new ResourceLocation( id );
	}

	@Override
	public String convert( ResourceLocation location ) {
		return location.toString();
	}
}
