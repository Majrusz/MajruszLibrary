package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnResourcesReloaded;
import com.majruszlibrary.events.base.Events;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin( ReloadableServerResources.class )
public abstract class MixinReloadableServerResources {
	@Inject(
		at = @At( "RETURN" ),
		method = "loadResources (Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/RegistryAccess$Frozen;Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/commands/Commands$CommandSelection;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
	)
	private static void loadResources( ResourceManager $$0, RegistryAccess.Frozen $$1, FeatureFlagSet $$2, Commands.CommandSelection $$3, int $$4, Executor $$5,
		Executor $$6, CallbackInfoReturnable< CompletableFuture< ReloadableServerResources > > callback
	) {
		callback.getReturnValue().whenComplete( ( resources, exception )->{
			Events.dispatch( new OnResourcesReloaded() );
		} );
	}
}
