package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnGameInitialized;
import com.majruszlibrary.events.base.Events;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( DedicatedServer.class )
public abstract class MixinDedicatedServer {
	@Inject(
		at = @At( "TAIL" ),
		method = "<init> (Ljava/lang/Thread;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/server/WorldStem;Lnet/minecraft/server/dedicated/DedicatedServerSettings;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/server/Services;Lnet/minecraft/server/level/progress/ChunkProgressListenerFactory;)V"
	)
	private void constructor( Thread $$0, LevelStorageSource.LevelStorageAccess $$1, PackRepository $$2, WorldStem $$3, DedicatedServerSettings $$4,
		DataFixer $$5, Services $$6, ChunkProgressListenerFactory $$7, CallbackInfo callback
	) {
		Events.dispatch( new OnGameInitialized() );
	}
}
