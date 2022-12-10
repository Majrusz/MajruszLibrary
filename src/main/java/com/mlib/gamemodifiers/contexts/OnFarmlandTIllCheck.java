package com.mlib.gamemodifiers.contexts;

import com.mlib.events.FarmlandTillCheckEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class OnFarmlandTIllCheck {
	public static final Consumer< Data > INCREASE_AREA = data->++data.event.area;

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
		public static void onCheck( FarmlandTillCheckEvent event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< FarmlandTillCheckEvent > {
		public Data( FarmlandTillCheckEvent event ) {
			super( event.player, event );
		}
	}
}
