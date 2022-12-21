package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class OnEquipmentChanged {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, String name, String comment ) {
			super( consumer, name, comment );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, "", "" );
		}

		@SubscribeEvent
		public static void onEquipmentChanged( LivingEquipmentChangeEvent event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< LivingEquipmentChangeEvent > {
		public final LivingEntity entity;

		public Data( LivingEquipmentChangeEvent event ) {
			super( event.getEntity(), event );
			this.entity = event.getEntity();
		}
	}
}
