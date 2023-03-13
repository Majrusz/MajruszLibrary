package com.mlib.gamemodifiers.contexts;

import com.mlib.Utility;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.Context;
import com.mlib.gamemodifiers.Contexts;
import com.mlib.gamemodifiers.data.ILevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class OnPlayerInteract {
	public static Context< Data > listen( Consumer< Data > consumer ) {
		return Contexts.get( Data.class ).add( consumer );
	}

	@SubscribeEvent
	public static void onEntityInteract( PlayerInteractEvent.EntityInteract event ) {
		Contexts.get( Data.class ).dispatch( new Data( event, Utility.castIfPossible( LivingEntity.class, event.getTarget() ) ) );
	}

	@SubscribeEvent
	public static void onRightClickBlock( PlayerInteractEvent.RightClickBlock event ) {
		Contexts.get( Data.class ).dispatch( new Data( event, event.getEntity() ) );
	}

	@SubscribeEvent
	public static void onRightClickItem( PlayerInteractEvent.RightClickItem event ) {
		Contexts.get( Data.class ).dispatch( new Data( event, event.getEntity() ) );
	}

	public static Condition< Data > isEntityInteraction() {
		return new Condition< Data >( data->data.event instanceof PlayerInteractEvent.EntityInteract );
	}

	public static Condition< Data > isBlockInteraction() {
		return new Condition< Data >( data->data.event instanceof PlayerInteractEvent.RightClickBlock );
	}

	public static Condition< Data > isItemInteraction() {
		return new Condition< Data >( data->data.event instanceof PlayerInteractEvent.RightClickItem );
	}

	public static Condition< Data > hasFace() {
		return new Condition< Data >( data->data.face != null );
	}

	public static class Data implements ILevelData {
		public final PlayerInteractEvent event;
		public final ItemStack itemStack;
		public final Player player;
		@Nullable public final LivingEntity target;
		public final InteractionHand hand;
		@Nullable public final Direction face;
		public final BlockPos position;

		public Data( PlayerInteractEvent event, @Nullable LivingEntity target ) {
			this.event = event;
			this.itemStack = event.getItemStack();
			this.player = event.getEntity();
			this.target = target;
			this.hand = event.getHand();
			this.face = event.getFace();
			this.position = event.getPos();
		}

		@Override
		public Level getLevel() {
			return this.player.getLevel();
		}
	}
}