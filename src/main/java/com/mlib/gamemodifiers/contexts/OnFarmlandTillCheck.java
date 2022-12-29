package com.mlib.gamemodifiers.contexts;

import com.mlib.events.FarmlandTillCheckEvent;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnFarmlandTillCheck {
	public static final Consumer< Data > INCREASE_AREA = data->++data.event.area;
	public static final Predicate< Data > IS_NOT_CROUCHING = data->!data.player.isCrouching();

	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer ) {
			super( consumer );

			CONTEXTS.add( this );
		}

		@SubscribeEvent
		public static void onCheck( FarmlandTillCheckEvent event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< FarmlandTillCheckEvent > {
		public final Player player;
		public final ItemStack itemStack;

		public Data( FarmlandTillCheckEvent event ) {
			super( event.player, event );
			this.player = event.player;
			this.itemStack = event.itemStack;
		}
	}
}
