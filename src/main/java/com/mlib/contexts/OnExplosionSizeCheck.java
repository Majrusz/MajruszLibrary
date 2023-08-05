package com.mlib.contexts;

import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.ILevelData;
import com.mlib.contexts.data.IPositionData;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnExplosionSizeCheck {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onExplosion( ExplosionEvent.Start explosionEvent ) {
		Level level = explosionEvent.getLevel();
		Explosion explosion = explosionEvent.getExplosion();
		Data data = Contexts.get( Data.class ).dispatch( new Data( level, explosion ) );

		if( ( data.size / explosion.radius ) > 1.25 && level instanceof ServerLevel serverLevel ) {
			sendToPlayers( serverLevel, explosion, explosion.getPosition(), data.size );
		}

		explosion.radius = data.size;
		explosion.fire = data.causesFire;
	}

	private static void sendToPlayers( ServerLevel level, Explosion explosion, Vec3 position, float size ) {
		double x = position.x, y = position.y, z = position.z;
		for( ServerPlayer player : level.players() ) {
			if( player.distanceToSqr( x, y, z ) < 4096.0 ) {
				player.connection.send( new ClientboundExplodePacket( x, y, z, size, explosion.getToBlow(), explosion.getHitPlayers().get( player ) ) );
			}
		}
	}

	public static class Data implements ILevelData, IPositionData {
		public final Level level;
		public final Explosion explosion;
		public float size;
		public boolean causesFire;

		public Data( Level level, Explosion explosion ) {
			this.level = level;
			this.explosion = explosion;
			this.size = explosion.radius;
			this.causesFire = explosion.fire;
		}

		@Override
		public Level getLevel() {
			return this.level;
		}

		@Override
		public Vec3 getPosition() {
			return this.explosion.getPosition();
		}
	}
}
