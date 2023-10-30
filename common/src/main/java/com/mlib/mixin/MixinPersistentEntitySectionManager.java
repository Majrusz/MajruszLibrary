package com.mlib.mixin;

import com.mlib.contexts.OnEntitySpawned;
import com.mlib.contexts.base.Contexts;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin( PersistentEntitySectionManager.class )
public abstract class MixinPersistentEntitySectionManager< Type extends EntityAccess > {
	private final Long2ObjectMap< Pair< Entity, Boolean > > mlibPendingContexts = new Long2ObjectOpenHashMap<>();

	@Inject(
		at = @At( "RETURN" ),
		method = "addEntity (Lnet/minecraft/world/level/entity/EntityAccess;Z)Z"
	)
	private void addEntity( Type entityAccess, boolean isLoadedFromDisk, CallbackInfoReturnable< Boolean > callback ) {
		if( callback.getReturnValue() && entityAccess instanceof Entity entity ) {
			this.mlibPendingContexts.put( entity.getId(), Pair.of( entity, isLoadedFromDisk ) );
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		List< Long > ids = new ArrayList<>();
		this.mlibPendingContexts.forEach( ( id, context )->{
			if( Contexts.dispatch( new OnEntitySpawned( context.getFirst(), context.getSecond() ) ).isSpawnCancelled() ) {
				context.getFirst().remove( Entity.RemovalReason.DISCARDED );
			}
			ids.add( id );
		} );
		ids.forEach( this.mlibPendingContexts::remove );
	}
}
