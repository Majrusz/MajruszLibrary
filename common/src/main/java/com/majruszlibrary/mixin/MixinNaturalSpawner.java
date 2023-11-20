package com.majruszlibrary.mixin;

import com.majruszlibrary.contexts.OnMobSpawnRateGet;
import com.majruszlibrary.contexts.base.Contexts;
import com.majruszlibrary.math.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( NaturalSpawner.class )
public abstract class MixinNaturalSpawner {
	@Inject(
		at = @At( "HEAD" ),
		cancellable = true,
		method = "spawnCategoryForChunk (Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V"
	)
	private static void spawnCategoryForChunk( MobCategory category, ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnPredicate predicate,
		NaturalSpawner.AfterSpawnCallback spawnCallback, CallbackInfo callbackInfo
	) {
		int mobsCount = Random.round( Contexts.dispatch( new OnMobSpawnRateGet( category, level, chunk ) ).getSpawnRate() );
		if( mobsCount == 0 ) {
			callbackInfo.cancel();
		}

		int extraMobsCount = mobsCount - 1;
		for( int idx = 0; idx < extraMobsCount; ++idx ) {
			NaturalSpawner.spawnCategoryForPosition( category, level, chunk, majruszlibrary$getRandomSpawnPos( level, chunk ), predicate, spawnCallback );
		}
	}

	private static BlockPos majruszlibrary$getRandomSpawnPos( ServerLevel level, LevelChunk chunk ) {
		ChunkPos chunkpos = chunk.getPos();
		int x = chunkpos.getMinBlockX() + Random.nextInt( 16 );
		int z = chunkpos.getMinBlockZ() + Random.nextInt( 16 );
		int y = Random.nextInt( level.getMinBuildHeight(), chunk.getHeight( Heightmap.Types.WORLD_SURFACE, x, z ) + 1 );

		return new BlockPos( x, y, z );
	}
}
