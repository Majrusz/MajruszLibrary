package com.mlib.network;

import com.mlib.data.ISerializable;
import com.mlib.modhelper.ModHelper;
import com.mlib.platform.Services;

import java.util.ArrayList;
import java.util.List;

public class NetworkHandler {
	static final INetworkPlatform PLATFORM = Services.load( INetworkPlatform.class );
	final ModHelper helper;
	final List< NetworkObject< ? > > objects = new ArrayList<>();

	public NetworkHandler( ModHelper helper ) {
		this.helper = helper;
	}

	public < Type extends ISerializable > NetworkObject< Type > create( String id, Class< Type > clazz ) {
		NetworkObject< Type > object = new NetworkObject<>( this, this.helper.getLocation( id ), clazz );
		this.objects.add( object );

		return object;
	}

	public void register() {
		PLATFORM.register( this.objects );
	}
}
