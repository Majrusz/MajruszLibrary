package com.mlib.events;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;

/** Event called when explosion is created to replace its size. */
@Mod.EventBusSubscriber
public class ExplosionSizeEvent extends Event implements IModBusEvent {
	public final Level level;
	public final Explosion explosion;
	public float size;
	public boolean causesFire;

	public ExplosionSizeEvent( Level level, Explosion explosion ) {
		this.level = level;
		this.explosion = explosion;
		this.size = this.explosion.radius;
		this.causesFire = this.explosion.fire;
	}

	@SubscribeEvent
	public static void onExplosion( ExplosionEvent.Start explosionEvent ) {
		Level level = explosionEvent.getLevel();
		Explosion explosion = explosionEvent.getExplosion();

		ExplosionSizeEvent event = new ExplosionSizeEvent( level, explosion );
		MinecraftForge.EVENT_BUS.post( event );

		if( ( event.size / explosion.radius ) > 1.25 && level instanceof ServerLevel )
			sendToPlayers( ( ServerLevel )level, explosion, explosion.getPosition(), event.size );

		explosion.radius = event.size;
		explosion.fire = event.causesFire;
	}

	/** Sends information about bigger explosion to all players nearby. */
	private static void sendToPlayers( ServerLevel level, Explosion explosion, Vec3 position, float size ) {
		double x = position.x, y = position.y, z = position.z;
		for( ServerPlayer player : level.players() )
			if( player.distanceToSqr( x, y, z ) < 4096.0 )
				player.connection.send( new ClientboundExplodePacket( x, y, z, size, explosion.getToBlow(), explosion.getHitPlayers().get( player ) ) );
	}
}
