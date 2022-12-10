package com.mlib.gamemodifiers.contexts;

import com.mlib.events.BlockSmeltCheckEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class OnBlockSmeltCheck {
	public static final Consumer< Data > ENABLE_SMELT = data->data.event.shouldSmelt = true;

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
		public static void onCheck( BlockSmeltCheckEvent event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< BlockSmeltCheckEvent > {
		public Data( BlockSmeltCheckEvent event ) {
			super( event.player, event );
		}
	}
}
