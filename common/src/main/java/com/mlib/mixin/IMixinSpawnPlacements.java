package com.mlib.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( SpawnPlacements.class )
public interface IMixinSpawnPlacements {
	@Invoker( "register" )
	static < T extends Mob > void register( EntityType< T > entityType, SpawnPlacements.Type type, Heightmap.Types heightmap,
		SpawnPlacements.SpawnPredicate< T > predicate
	) {
		throw new AssertionError();
	}
}
