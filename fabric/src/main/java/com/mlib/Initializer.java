package com.mlib;

import com.mlib.mixininterfaces.fabric.IMixinLootTableBuilder;
import com.mlib.platform.PlatformFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

public class Initializer implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register( server->PlatformFabric.SERVER = server );
		ServerLifecycleEvents.SERVER_STOPPED.register( server->PlatformFabric.SERVER = null );
		LootTableEvents.MODIFY.register( ( resourceManager, lootManager, id, builder, source )->( ( IMixinLootTableBuilder )builder ).set( id ) );
		MajruszLibrary.HELPER.register();
	}
}
