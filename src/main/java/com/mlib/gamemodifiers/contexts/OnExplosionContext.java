package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.data.OnExplosionData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

// this context is executed twice! (start and detonate)
@Mod.EventBusSubscriber
public class OnExplosionContext extends ContextBase< OnExplosionData > {
	static final List< OnExplosionContext > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

	public OnExplosionContext( Consumer< OnExplosionData > consumer, ContextParameters params ) {
		super( OnExplosionData.class, consumer, params );
		ContextBase.addSorted( CONTEXTS, this );
	}

	public OnExplosionContext( Consumer< OnExplosionData > consumer ) {
		this( consumer, new ContextParameters() );
	}

	@SubscribeEvent
	public static void onExplosionStart( ExplosionEvent.Start event ) {
		OnExplosionData data = new OnExplosionData( event );
		ContextBase.accept( CONTEXTS, data );
		updateEvent( data );
	}

	@SubscribeEvent
	public static void onExplosionDetonate( ExplosionEvent.Detonate event ) {
		ContextBase.accept( CONTEXTS, new OnExplosionData( event ) );
	}

	private static void updateEvent( OnExplosionData data ) {
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
