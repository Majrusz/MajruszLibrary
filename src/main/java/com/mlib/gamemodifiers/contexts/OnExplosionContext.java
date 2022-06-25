package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// this context is executed twice! (start and detonate)
@Mod.EventBusSubscriber
public class OnExplosionContext extends Context {
	static final List< OnExplosionContext > CONTEXTS = new ArrayList<>();

	public OnExplosionContext( String configName, String configComment ) {
		super( configName, configComment );
		CONTEXTS.add( this );
	}

	public OnExplosionContext() {
		this( "OnExplosion", "" );
	}

	@SubscribeEvent
	public static void onExplosionStart( ExplosionEvent.Start event ) {
		Data data = new Data( event );
		handleContexts( data, CONTEXTS );
		updateEvent( data );
	}

	@SubscribeEvent
	public static void onExplosionDetonate( ExplosionEvent.Detonate event ) {
		handleContexts( new Data( event ), CONTEXTS );
	}

	public static class Data extends Context.Data {
		public final ExplosionEvent event;
		public final Explosion explosion;
		@Nullable
		public final LivingEntity sourceMob;
		@Nullable
		public final ServerLevel level;
		public final MutableFloat radius;
		public final MutableBoolean causesFire;

		public Data( ExplosionEvent event ) {
			super( event.getExplosion().getSourceMob() );
			this.event = event;
			this.explosion = event.getExplosion();
			this.sourceMob = this.explosion.getSourceMob();
			this.level = Utility.castIfPossible( ServerLevel.class, event.getWorld() );
			this.radius = new MutableFloat( this.explosion.radius );
			this.causesFire = new MutableBoolean( this.explosion.fire );
		}
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
