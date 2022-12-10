package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.time.Delay;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnSpawned {
	public static final Predicate< Data > IS_NOT_LOADED_FROM_DISK = data->!data.loadedFromDisk;

	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, String name, String comment ) {
			super( consumer, name, comment );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, "", "" );
		}

		@SubscribeEvent
		public static void onSpawn( EntityJoinLevelEvent event ) {
			if( !( event.getEntity() instanceof LivingEntity entity ) || !( event.getLevel() instanceof ServerLevel ) )
				return;

			// it does not contain an event, and it is delayed on purpose because otherwise it could cause deadlocks on chunks with any incorrect access (see EntityJoinWorldEvent for more info)
			Delay.onNextTick( ()->CONTEXTS.accept( new Data( entity, event.loadedFromDisk() ) ) );
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
}