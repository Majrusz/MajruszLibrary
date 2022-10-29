package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.events.BlockSmeltCheckEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OnBlockSmeltCheck {
	public static final Consumer< Data > ENABLE_SMELT = data->data.event.shouldSmelt = true;

	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final List< Context > CONTEXTS = Collections.synchronizedList( new ArrayList<>() );

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( Data.class, consumer, params );
			ContextBase.addSorted( CONTEXTS, this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onCheck( BlockSmeltCheckEvent event ) {
			ContextBase.accept( CONTEXTS, new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< BlockSmeltCheckEvent > {
		public Data( BlockSmeltCheckEvent event ) {
			super( Utility.castIfPossible( LivingEntity.class, event.player ), event );
		}
	}
}
