package com.mlib.gamemodifiers.contexts;

import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class OnItemFished {
	@Mod.EventBusSubscriber
	public static class Context extends ContextBase< Data > {
		static final Contexts< Data, Context > CONTEXTS = new Contexts<>();

		public Context( Consumer< Data > consumer, ContextParameters params ) {
			super( Data.class, consumer, params );
			CONTEXTS.add( this );
		}

		public Context( Consumer< Data > consumer ) {
			this( consumer, new ContextParameters() );
		}

		@SubscribeEvent
		public static void onDimensionChanged( ItemFishedEvent event ) {
			CONTEXTS.accept( new Data( event ) );
		}
	}

	public static class Data extends ContextData.Event< ItemFishedEvent > {
		public final Player player;
		public final FishingHook hook;
		public final NonNullList< ItemStack > drops;

		public Data( ItemFishedEvent event ) {
			super( event.getEntity(), event );
			this.player = event.getEntity();
			this.hook = event.getHookEntity();
			this.drops = event.getDrops();
		}
	}
}