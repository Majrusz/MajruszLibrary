package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.data.OnLootTableCustomLoadData;
import com.mlib.gamemodifiers.parameters.ContextParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnLootTableCustomLoadContext extends Context< OnLootTableCustomLoadData > {
	static final List< OnLootTableCustomLoadContext > CONTEXTS = new ArrayList<>();

	public OnLootTableCustomLoadContext( Consumer< OnLootTableCustomLoadData > consumer, ContextParameters params ) {
		super( OnLootTableCustomLoadData.class, consumer, params );
		Context.addSorted( CONTEXTS, this );
	}

	public OnLootTableCustomLoadContext( Consumer< OnLootTableCustomLoadData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	public static void broadcast( OnLootTableCustomLoadData data ) {
		Context.accept( CONTEXTS, data );
	}
}
