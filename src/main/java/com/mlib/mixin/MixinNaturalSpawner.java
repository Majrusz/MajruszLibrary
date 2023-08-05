package com.mlib.mixin;

import com.mlib.Random;
import com.mlib.contexts.OnMobSpawnRate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( NaturalSpawner.class )
public abstract class MixinNaturalSpawner {
	@Shadow( aliases = { "this$0" } )
	@Inject( method = "spawnCategoryForChunk (Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V", at = @At( "HEAD" ), cancellable = true )
	private static void spawnCategoryForChunk( MobCategory category, ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnPredicate predicate,
		NaturalSpawner.AfterSpawnCallback spawnCallback, CallbackInfo callbackInfo
	) {
		int extraMobsCount = Random.roundRandomly( OnMobSpawnRate.dispatch( category, level, chunk ).getSpawnRate() - 1.0f );
		for( int idx = 0; idx < extraMobsCount; ++idx ) {
			NaturalSpawner.spawnCategoryForPosition( category, level, chunk, getRandomSpawnPos( level, chunk ), predicate, spawnCallback );
		}
	}

	private static BlockPos getRandomSpawnPos( ServerLevel level, LevelChunk chunk ) {
		ChunkPos chunkpos = chunk.getPos();
		int x = chunkpos.getMinBlockX() + Random.nextInt( 16 );
		int z = chunkpos.getMinBlockZ() + Random.nextInt( 16 );
		int y = Random.nextInt( level.getMinBuildHeight(), chunk.getHeight( Heightmap.Types.WORLD_SURFACE, x, z ) + 1 );

		return new BlockPos( x, y, z );
	}
}
