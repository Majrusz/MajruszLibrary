package com.mlib.mixin;

import com.mlib.MajruszLibrary;
import com.mlib.contexts.OnEntitySpawned;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.function.Supplier;

@Mixin( PersistentEntitySectionManager.class )
public abstract class MixinPersistentEntitySectionManager< Type extends EntityAccess > {
	private final Supplier< ArrayList< Pair< Entity, Boolean > > > mlibPendingContexts = MajruszLibrary.SIDE.createSideDependent( ArrayList::new );

	@Inject(
		at = @At( "HEAD" ),
		method = "addEntity (Lnet/minecraft/world/level/entity/EntityAccess;Z)Z"
	)
	public void addEntity( Type entityAccess, boolean isLoadedFromDisk, CallbackInfoReturnable< Boolean > callback ) {
		if( entityAccess instanceof Entity entity ) {
			this.mlibPendingContexts.get().add( Pair.of( entity, isLoadedFromDisk ) );
		}
	}

	@Inject(
		at = @At( "HEAD" ),
		method = "tick ()V"
	)
	public void tick( CallbackInfo callback ) {
		this.mlibPendingContexts.get().forEach( context->{
			if( OnEntitySpawned.dispatch( context.getFirst(), context.getSecond() ).isSpawnCancelled() ) {
				context.getFirst().remove( Entity.RemovalReason.DISCARDED );
			}
		} );
		this.mlibPendingContexts.get().clear();
	}
}
