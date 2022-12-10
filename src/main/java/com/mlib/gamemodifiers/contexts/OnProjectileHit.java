package com.mlib.gamemodifiers.contexts;

import com.mlib.events.ProjectileEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OnProjectileHit {
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
		public static void onProjectileHit( ProjectileEvent.Hit event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends OnProjectileShot.Data {
		@Nullable public final EntityHitResult entityHitResult;
		@Nullable public final BlockHitResult blockHitResult;

		public Data( ProjectileEvent.Hit event ) {
			super( event );
			this.entityHitResult = event.entityHitResult;
			this.blockHitResult = event.blockHitResult;
		}
	}
}
