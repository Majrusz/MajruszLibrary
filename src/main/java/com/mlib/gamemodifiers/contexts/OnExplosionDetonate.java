package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.ExplosionEvent;
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

	public static class Data implements ILevelData {
		public final ExplosionEvent.Detonate event;
		public final Explosion explosion;
		@Nullable public final LivingEntity sourceMob;

		public Data( ExplosionEvent.Detonate event ) {
			this.event = event;
			this.explosion = event.getExplosion();
			this.sourceMob = this.explosion.getIndirectSourceEntity();
		}

		@Override
		public Level getLevel() {
			return this.event.getLevel();
		}
	}
}
