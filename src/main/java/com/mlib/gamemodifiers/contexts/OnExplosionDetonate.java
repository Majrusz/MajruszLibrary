package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import com.mlib.gamemodifiers.data.IPositionData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnExplosionDetonate {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onExplosionDetonate( ExplosionEvent.Detonate event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData, IPositionData {
		public final ExplosionEvent.Detonate event;
		public final Explosion explosion;
		@Nullable public final LivingEntity sourceMob;

		public Data( ExplosionEvent.Detonate event ) {
			this.event = event;
			this.explosion = event.getExplosion();
			this.sourceMob = this.explosion.getSourceMob();
		}

		@Override
		public Level getLevel() {
			return this.event.getWorld();
		}

		@Override
		public Vec3 getPosition() {
			return this.explosion.getPosition();
		}
	}
}
