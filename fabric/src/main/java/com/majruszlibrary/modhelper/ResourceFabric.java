package com.majruszlibrary.modhelper;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceFabric implements IResourcePlatform {
	@Override
	public void register( ResourceLocation id, ResourceLoader.Server server ) {
		ResourceManagerHelper.get( PackType.SERVER_DATA ).registerReloadListener( new IdentifiableResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return id;
			}

			@Override
			public CompletableFuture< Void > reload( PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller,
				ProfilerFiller profilerFiller2, Executor executor, Executor executor2
			) {
				return server.reload( preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2 );
			}
		} );
	}
}
