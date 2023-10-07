package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OnResourceListenersGet {
	public final List< PreparableReloadListener > listeners = new ArrayList<>();

	public static Context< OnResourceListenersGet > listen( Consumer< OnResourceListenersGet > consumer ) {
		return Contexts.get( OnResourceListenersGet.class ).add( consumer );
	}

	public OnResourceListenersGet() {}
}
