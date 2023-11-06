package com.mlib.network;

import com.mlib.modhelper.ModHelper;
import com.mlib.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NetworkHandler {
	static final INetworkPlatform PLATFORM = Services.load( INetworkPlatform.class );
	final ModHelper helper;
	final List< NetworkObject< ? > > objects = new ArrayList<>();

	public NetworkHandler( ModHelper helper ) {
		this.helper = helper;
	}

	public < Type > NetworkObject< Type > create( String id, Class< Type > clazz, Supplier< Type > instance ) {
		NetworkObject< Type > object = new NetworkObject<>( this, this.helper.getLocation( id ), clazz, instance );
		this.objects.add( object );

		return object;
	}

	public void register() {
		PLATFORM.register( this.helper, this.objects );
	}
}
