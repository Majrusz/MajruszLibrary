package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 BE CAREFUL!
 .
 This context is usually handled when world chunks are being loaded and most world modifications
 like adding new mobs, checking chunk/structure info etc. may cause deadlocks on these chunks.
 Use ContextSafe whenever a world interaction is required! (see EntityJoinLevelEvent for more info)
 */
public class OnSpawned {
	@Deprecated( since = "3.2.0 (use IsNotLoadedFromDisk class instead)", forRemoval = true )
	public static final Predicate< Data > IS_NOT_LOADED_FROM_DISK = data->!data.loadedFromDisk;

	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}

		@SubscribeEvent
		public static void onSpawn( EntityJoinWorldEvent event ) {
			if( !( event.getEntity() instanceof LivingEntity entity ) )
				return;

			CONTEXTS.accept( new Data( entity, event.loadedFromDisk() ) );
		}
	}

	@Mod.EventBusSubscriber
	public static class ContextSafe extends ContextBase< Data > {
		static final Contexts< Data, ContextSafe > CONTEXTS = new Contexts<>();
		static final List< Data > CLIENT_PENDING_LIST = new ArrayList<>();
		static final List< Data > SERVER_PENDING_LIST = new ArrayList<>();

		public ContextSafe( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}

		@SubscribeEvent
		public static void onSpawn( EntityJoinLevelEvent event ) {
			if( !( event.getEntity() instanceof LivingEntity entity ) )
				return;

			List< Data > list = entity.level.isClientSide ? CLIENT_PENDING_LIST : SERVER_PENDING_LIST;
			list.add( new Data( entity, event.loadedFromDisk() ) );
		}

		@SubscribeEvent
		public static void onClientTickEnd( TickEvent.ClientTickEvent event ) {
			if( event.phase != TickEvent.Phase.END )
				return;

			handle( CLIENT_PENDING_LIST );
		}

		@SubscribeEvent
		public static void onServerTickEnd( TickEvent.ServerTickEvent event ) {
			if( event.phase != TickEvent.Phase.END )
				return;

			handle( SERVER_PENDING_LIST );
		}

		private static void handle( List< Data > pendingList ) {
			List< Data > list;
			synchronized( pendingList ) {
				list = new ArrayList<>( pendingList );
				pendingList.clear();
			}
			list.forEach( data->{
				if( data.target.isAddedToWorld() ) {
					CONTEXTS.accept( data );
				}
			} );
		}
	}

	public static class Data extends ContextData {
		public final LivingEntity target;
		public final boolean loadedFromDisk;

		public Data( LivingEntity target, boolean loadedFromDisk ) {
			super( target );

			this.target = target;
			this.loadedFromDisk = loadedFromDisk;
		}
	}

	public static class IsNotLoadedFromDisk< Type extends Data > extends Condition< Type > {
		@Override
		protected boolean check( GameModifier feature, Type data ) {
			return !data.loadedFromDisk;
		}
	}
}