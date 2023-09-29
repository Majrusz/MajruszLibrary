package net.mlib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.mlib.mixininterfaces.fabric.IMixinLootTableBuilder;
import net.mlib.platform.ModSideFabric;

public class Initializer implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register( server->ModSideFabric.SERVER = server );
		ServerLifecycleEvents.SERVER_STOPPED.register( server->ModSideFabric.SERVER = null );
		LootTableEvents.MODIFY.register( ( resourceManager, lootManager, id, builder, source )->( ( IMixinLootTableBuilder )builder ).set( id ) );
		MajruszLibrary.HELPER.register();
	}
}
