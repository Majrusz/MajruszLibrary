package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import com.mlib.gamemodifiers.data.IPositionData;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnExplosionStart {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onExplosionStart( ExplosionEvent.Start event ) {
		Data data = Contexts.get( Data.class ).dispatch( new Data( event ) );
		updateEvent( data );
	}

	private static void updateEvent( Data data ) {
		if( ( data.radius.getValue() - data.explosion.radius ) < 1.0 || !( data.getLevel() instanceof ServerLevel level ) ) {
			return;
		}

		data.explosion.radius = data.radius.getValue();
		data.explosion.fire = data.causesFire.getValue();
		double x = data.explosion.getPosition().x, y = data.explosion.getPosition().y, z = data.explosion.getPosition().z;
		for( ServerPlayer player : level.players() )
			if( player.distanceToSqr( x, y, z ) < 4096.0 )
				player.connection.send( new ClientboundExplodePacket( x, y, z, data.explosion.radius, data.explosion.getToBlow(), data.explosion.getHitPlayers()
					.get( player ) ) );
	}

	public static class Data implements ILevelData, IPositionData {
		public final ExplosionEvent.Start event;
		public final Explosion explosion;
		@Nullable public final LivingEntity sourceMob;
		public final MutableFloat radius;
		public final MutableBoolean causesFire;

		public Data( ExplosionEvent.Start event ) {
			this.event = event;
			this.explosion = event.getExplosion();
			this.sourceMob = this.explosion.getSourceMob();
			this.radius = new MutableFloat( this.explosion.radius );
			this.causesFire = new MutableBoolean( this.explosion.fire );
		}

		@Override
		public Level getLevel() {
			return this.event.getLevel();
		}

		@Override
		public Vec3 getPosition() {
			return this.explosion.getPosition();
		}
	}
}
