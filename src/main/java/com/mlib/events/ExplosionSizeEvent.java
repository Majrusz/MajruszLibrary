package com.mlib.events;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

/** Event called when explosion is created to replace its size. */
@Mod.EventBusSubscriber
public class ExplosionSizeEvent extends Event implements IModBusEvent {
	public final World world;
	public final Explosion explosion;
	public float size;
	public boolean causesFire;

	public ExplosionSizeEvent( World world, Explosion explosion ) {
		this.world = world;
		this.explosion = explosion;
		this.size = this.explosion.size;
		this.causesFire = this.explosion.causesFire;
	}

	@SubscribeEvent
	public static void onExplosion( ExplosionEvent.Start explosionEvent ) {
		World world = explosionEvent.getWorld();
		Explosion explosion = explosionEvent.getExplosion();

		ExplosionSizeEvent event = new ExplosionSizeEvent( world, explosion );
		MinecraftForge.EVENT_BUS.post( event );

		if( ( event.size / explosion.size ) > 1.25 && world instanceof ServerWorld )
			sendToPlayers( ( ServerWorld )world, explosion, explosion.getPosition(), event.size );

		explosion.size = event.size;
		explosion.causesFire = event.causesFire;
	}

	/** Sends information about bigger explosion to all players nearby. */
	private static void sendToPlayers( ServerWorld world, Explosion explosion, Vector3d position, float size ) {
		double x = position.x, y = position.y, z = position.z;
		for( ServerPlayerEntity player : world.getPlayers() )
			if( player.getDistanceSq( x, y, z ) < 4096.0 )
				player.connection.sendPacket( new SExplosionPacket( x, y, z, size, explosion.getAffectedBlockPositions(),
					explosion.getPlayerKnockbackMap()
						.get( player )
				) );
	}
}
