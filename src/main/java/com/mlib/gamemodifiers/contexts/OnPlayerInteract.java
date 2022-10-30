package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.ContextBase;
import com.mlib.gamemodifiers.ContextData;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.parameters.ContextParameters;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class OnPlayerInteract {
	public static final Predicate< Data > IS_ENTITY_INTERACTION = data->data.event instanceof PlayerInteractEvent.EntityInteract;
	public static final Predicate< Data > IS_BLOCK_INTERACTION = data->data.event instanceof PlayerInteractEvent.RightClickBlock;
	public static final Predicate< Data > IS_ITEM_INTERACTION = data->data.event instanceof PlayerInteractEvent.RightClickItem;

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
		public static void onEntityInteract( PlayerInteractEvent.EntityInteract event ) {
			CONTEXTS.accept( new Data( event, Utility.castIfPossible( LivingEntity.class, event.getTarget() ) ) );
		}

		@SubscribeEvent
		public static void onRightClickBlock( PlayerInteractEvent.RightClickBlock event ) {
			CONTEXTS.accept( new Data( event, event.getEntity() ) );
		}

		@SubscribeEvent
		public static void onRightClickItem( PlayerInteractEvent.RightClickItem event ) {
			CONTEXTS.accept( new Data( event, event.getEntity() ) );
		}
	}

	public static class Data extends ContextData.Event< PlayerInteractEvent > {
		public final ItemStack itemStack;
		public final Player player;
		@Nullable public final LivingEntity target;
		public final InteractionHand hand;

		public Data( PlayerInteractEvent event, @Nullable LivingEntity target ) {
			super( event.getEntity(), event );
			this.itemStack = event.getItemStack();
			this.player = event.getEntity();
			this.target = target;
			this.hand = event.getHand();
		}
	}
}