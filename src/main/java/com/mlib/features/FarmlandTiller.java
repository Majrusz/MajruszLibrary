package com.mlib.features;

import com.mlib.LevelHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** Tills grass in greater area. */
@Mod.EventBusSubscriber
public class FarmlandTiller {
	public static final List< Register > registerList = new ArrayList<>();

	static {
		registerList.add( ( level, player, itemStack )->true );
	}

	@SubscribeEvent
	public static void onHoeUse( PlayerInteractEvent.RightClickBlock event ) {
		Player player = event.getPlayer();
		ServerLevel level = LevelHelper.getServerLevelFromEntity( player );
		BlockPos blockPosition = event.getPos();
		ItemStack itemStack = event.getItemStack();
		InteractionHand hand = event.getHand();
		Direction direction = event.getFace();
		if( level == null || event.getFace() == null )
			return;

		Data targetData = getData( level, player, blockPosition, hand, direction, 0, 0 );
		if( targetData.predicate == null || !targetData.predicate.test( targetData.context ) )
			return;

		int area = getArea( level, player, itemStack );
		for( int x = -area; x <= area; ++x )
			for( int z = -area; z <= area; ++z ) {
				Data offsetData = getData( level, player, blockPosition, hand, direction, x, z );

				if( offsetData.predicate != null && offsetData.consumer != null && offsetData.predicate.test( offsetData.context ) ) {
					offsetData.consumer.accept( offsetData.context );
					itemStack.hurtAndBreak( 1, player, entity->entity.broadcastBreakEvent( hand ) );
				}
			}
	}

	/** Returns tilling area from register list. */
	protected static int getArea( ServerLevel level, Player player, ItemStack itemStack ) {
		int areaSum = 0, areaMax = 0;
		for( Register register : registerList )
			if( register.shouldBeExecuted( level, player, itemStack ) ) {
				int area = register.getArea( level, player, itemStack );
				areaMax = Math.max( areaMax, area );

				if( register.isSummable( level, player, itemStack ) )
					areaSum = areaSum + area;
			}

		return Math.max( areaSum, areaMax );
	}

	/** Returns all required information about given block. */
	protected static Data getData( ServerLevel level, Player player, BlockPos blockPosition, InteractionHand hand, Direction direction, int x, int z
	) {
		BlockPos offsetPosition = blockPosition.offset( x, 0, z );
		BlockState blockState = level.getBlockState( offsetPosition );
		Pair< Predicate< UseOnContext >, Consumer< UseOnContext > > pair = HoeItem.TILLABLES.get( blockState.getBlock() );

		BlockHitResult hitResult = new BlockHitResult( player.position(), direction, offsetPosition, true );
		UseOnContext context = new UseOnContext( player, hand, hitResult );

		return pair != null ? new Data( pair.getFirst(), pair.getSecond(), context ) : new Data( null, null, context );
	}

	private static class Data {
		@Nullable
		public Predicate< UseOnContext > predicate;
		@Nullable
		public Consumer< UseOnContext > consumer;
		public UseOnContext context;

		public Data( Predicate< UseOnContext > predicate, Consumer< UseOnContext > consumer, UseOnContext context ) {
			this.predicate = predicate;
			this.consumer = consumer;
			this.context = context;
		}
	}

	@FunctionalInterface
	public interface Register {
		boolean shouldBeExecuted( ServerLevel level, Player player, ItemStack itemStack );

		default int getArea( ServerLevel level, Player player, ItemStack itemStack ) {
			return 1;
		}

		default boolean isSummable( ServerLevel level, Player player, ItemStack itemStack ) {
			return true;
		}
	}
}
