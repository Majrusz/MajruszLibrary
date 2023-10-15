package com.mlib.platform;

import java.util.function.Function;
import java.util.function.Supplier;

public class LogicalSafe< Type > implements Supplier< Type > {
	private Type client;
	private Type server;

	public static < Type > LogicalSafe< Type > of( Type client, Type server ) {
		return new LogicalSafe<>( client, server );
	}

	@Override
	public Type get() {
		if( Side.isLogicalClient() ) {
			return this.client;
		} else {
			return this.server;
		}
	}

	public void set( Type value ) {
		if( Side.isLogicalClient() ) {
			this.client = value;
		} else {
			this.server = value;
		}
	}

	public void set( Function< Type, Type > function ) {
		if( Side.isLogicalClient() ) {
			this.client = function.apply( this.client );
		} else {
			this.server = function.apply( this.server );
		}
	}

	private LogicalSafe( Type client, Type server ) {
		this.client = client;
		this.server = server;
	}
}
