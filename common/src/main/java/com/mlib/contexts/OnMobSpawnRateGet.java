package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Consumer;

public class OnMobSpawnRateGet implements IEntityData {
	public final MobCategory category;
	public final ServerLevel level;
	public final LevelChunk chunk;
	public final Player player;
	public final float original = 1.0f;
	public float value = 1.0f;

	public static Context< OnMobSpawnRateGet > listen( Consumer< OnMobSpawnRateGet > consumer ) {
		return Contexts.get( OnMobSpawnRateGet.class ).add( consumer );
	}

	public OnMobSpawnRateGet( MobCategory category, ServerLevel level, LevelChunk chunk ) {
		this.category = category;
		this.level = level;
		this.chunk = chunk;

		ChunkPos chunkPos = chunk.getPos();
		int x = chunkPos.getMinBlockX();
		int z = chunkPos.getMinBlockZ();
		int y = chunk.getHeight( Heightmap.Types.WORLD_SURFACE, x, z );
		this.player = level.getNearestPlayer( x, y, z, -1.0, false );
	}

	@Override
	public Entity getEntity() {
		return this.player;
	}

	public float getSpawnRate() {
		return Math.max( this.value, 0.0f );
	}
}
