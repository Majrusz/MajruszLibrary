package com.mlib.loot_modifiers;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

import javax.annotation.Nullable;

/**
 Common functions used with LootContext.
 */
public class LootHelper {
	/** Returns parameter from context if it exists or null otherwise. */
	@Nullable
	@Deprecated
	public static < Type > Type getParameter( LootContext context, LootContextParam< Type > parameter ) {
		return context.hasParam( parameter ) ? context.getParam( parameter ) : null;
	}
}
