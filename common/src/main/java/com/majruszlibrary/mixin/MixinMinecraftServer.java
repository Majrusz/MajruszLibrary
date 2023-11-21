package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnLevelsLoaded;
import com.majruszlibrary.events.OnServerTicked;
import com.majruszlibrary.events.OnTradesInitialized;
import com.majruszlibrary.events.base.Events;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin( MinecraftServer.class )
public abstract class MixinMinecraftServer {
	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			target = "Lnet/minecraft/server/MinecraftServer;tickChildren (Ljava/util/function/BooleanSupplier;)V",
			value = "INVOKE"
		),
		method = "tickServer (Ljava/util/function/BooleanSupplier;)V"
	)
	private void tickServer( BooleanSupplier haveTime, CallbackInfo callback ) {
		Events.dispatch( new OnServerTicked() );
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "loadLevel ()V"
	)
	private void loadLevel( CallbackInfo callback ) {
		for( VillagerProfession profession : VillagerTrades.TRADES.keySet() ) {
			Int2ObjectMap< List< VillagerTrades.ItemListing > > currentTrades = new Int2ObjectOpenHashMap<>();
			VillagerTrades.TRADES.get( profession ).forEach( ( tier, trades )->currentTrades.put( tier, new ArrayList<>( List.of( trades ) ) ) );
			Events.dispatch( new OnTradesInitialized( currentTrades, profession ) );
			Int2ObjectMap< VillagerTrades.ItemListing[] > newTrades = new Int2ObjectOpenHashMap<>();
			currentTrades.forEach( ( tier, trades )->newTrades.put( tier, trades.toArray( VillagerTrades.ItemListing[]::new ) ) );
			VillagerTrades.TRADES.put( profession, newTrades );
		}
	}

	@Inject(
		at = @At( "RETURN" ),
		method = "createLevels (Lnet/minecraft/server/level/progress/ChunkProgressListener;)V"
	)
	private void createLevels( ChunkProgressListener listener, CallbackInfo callback ) {
		Events.dispatch( new OnLevelsLoaded( ( MinecraftServer )( Object )this ) );
	}
}
