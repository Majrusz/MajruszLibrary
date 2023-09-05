package com.mlib.contexts;

import com.mlib.Utility;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.contexts.base.Contexts;
import com.mlib.contexts.data.IEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnDamaged {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onDamaged( LivingHurtEvent event ) {
		Contexts.get( Data.class ).dispatch( new Data( event ) );
	}

	public static Condition< Data > isDirect() {
		return new Condition<>( Data::isDirect );
	}

	public static Condition< Data > dealtAnyDamage() {
		return new Condition<>( Data::dealtAnyDamage );
	}

	public static class Data implements IEntityData {
		public final LivingHurtEvent event;
		public final DamageSource source;
		@Nullable public final LivingEntity attacker;
		public final LivingEntity target;

		public Data( LivingHurtEvent event ) {
			this.event = event;
			this.source = event.getSource();
			this.attacker = Utility.castIfPossible( LivingEntity.class, this.source.getEntity() );
			this.target = event.getEntity();
		}

		@Override
		public Entity getEntity() {
			return this.target;
		}

		public boolean isDirect() {
			return this.source.getDirectEntity() == this.attacker;
		}

		public boolean dealtAnyDamage() {
			return this.event.getAmount() > 0.0f;
		}
	}
}