package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OnExplosion {
	// this context is executed twice! (start and detonate)
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( consumer, params );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onExplosionStart( ExplosionEvent.Start event ) {
			Data data = new Data( event );
			CONTEXTS.accept( data );
			updateEvent( data );
		}

		@SubscribeEvent
		public static void onExplosionDetonate( ExplosionEvent.Detonate event ) {
			CONTEXTS.accept( new Data( event ) );
		}

		private static void updateEvent( Data data ) {
			if( ( data.radius.getValue() - data.explosion.radius ) < 1.0 || data.level == null ) {
				return;
			}

			data.explosion.radius = data.radius.getValue();
			data.explosion.fire = data.causesFire.getValue();
			double x = data.explosion.getPosition().x, y = data.explosion.getPosition().y, z = data.explosion.getPosition().z;
			for( ServerPlayer player : data.level.players() )
				if( player.distanceToSqr( x, y, z ) < 4096.0 )
					player.connection.send( new ClientboundExplodePacket( x, y, z, data.explosion.radius, data.explosion.getToBlow(), data.explosion.getHitPlayers()
						.get( player ) ) );
		}
	}

	public static class Data extends ContextData.Event< ExplosionEvent > {
		public final Explosion explosion;
		@Nullable public final LivingEntity sourceMob;
		public final MutableFloat radius;
		public final MutableBoolean causesFire;

		public Data( ExplosionEvent event ) {
			super( Utility.castIfPossible( ServerLevel.class, event.getLevel() ), event );
			this.explosion = event.getExplosion();
			this.sourceMob = this.explosion.getSourceMob();
			this.radius = new MutableFloat( this.explosion.radius );
			this.causesFire = new MutableBoolean( this.explosion.fire );
		}
	}
}
