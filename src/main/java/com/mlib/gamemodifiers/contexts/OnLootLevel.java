package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnLootLevel {
	public static ContextBase< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onLootLevel( LootingLevelEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static class Data implements ILevelData {
		public final LootingLevelEvent event;
		@Nullable public final DamageSource source;
		@Nullable public final LivingEntity attacker;

		public Data( LootingLevelEvent event ) {
			this.event = event;
			this.source = event.getDamageSource();
			this.attacker = this.source != null ? Utility.castIfPossible( LivingEntity.class, this.source.getEntity() ) : null;
		}

		@Override
		public Level getLevel() {
			return this.attacker != null ? this.attacker.getLevel() : null;
		}
	}
}