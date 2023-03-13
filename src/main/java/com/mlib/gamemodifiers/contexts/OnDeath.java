package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDeath {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onDamaged( LivingDeathEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData {
		public final LivingDeathEvent event;
		public final DamageSource source;
		@Nullable public final LivingEntity attacker;
		public final LivingEntity target;

		public Data( LivingDeathEvent event ) {
			this.event = event;
			this.source = event.getSource();
			this.attacker = Utility.castIfPossible( LivingEntity.class, source.getEntity() );
			this.target = event.getEntity();
		}

		@Override
		public Level getLevel() {
			return this.target.getLevel();
		}
	}

}