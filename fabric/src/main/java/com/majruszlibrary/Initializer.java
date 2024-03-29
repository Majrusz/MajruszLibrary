package com.majruszlibrary;

import com.majruszlibrary.mixininterfaces.fabric.IMixinLootTableBuilder;
import com.majruszlibrary.platform.SideFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

public class Initializer implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register( server->SideFabric.SERVER = server );
		ServerLifecycleEvents.SERVER_STOPPED.register( server->SideFabric.SERVER = null );
		LootTableEvents.MODIFY.register( ( resourceManager, lootManager, id, builder, source )->( ( IMixinLootTableBuilder )builder ).majruszlibrary$set( id ) );
		MajruszLibrary.HELPER.register();
	}
}
