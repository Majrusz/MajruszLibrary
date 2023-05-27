package com.mlib.features;

import com.mlib.annotations.AutoInstance;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.contexts.OnFarmlandTillCheck;
import com.mlib.gamemodifiers.contexts.OnPlayerInteract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@AutoInstance
public class FarmlandTiller {
	final Function< OnPlayerInteract.Data, Player > PLAYER_SUPPLIER = data->data.player;

	public FarmlandTiller() {
		OnPlayerInteract.listen( this::applyHoeBonus )
			.addCondition( Condition.isServer() )
			.addCondition( Condition.isShiftKeyDown( PLAYER_SUPPLIER ).negate() )
			.addCondition( OnPlayerInteract.isBlockInteraction() )
			.addCondition( OnPlayerInteract.hasFace() );
	}

	private void applyHoeBonus( OnPlayerInteract.Data data ) {
		OnFarmlandTillCheck.Data extraData = OnFarmlandTillCheck.dispatch( data.getServerLevel(), data.player, data.itemStack );
		if( extraData.area == 0 || this.getInfo( data.getServerLevel(), data.player, data.position, data.hand, data.face ) == null )
			return;

		for( int x = -extraData.area; x <= extraData.area; ++x ) {
			for( int z = -extraData.area; z <= extraData.area; ++z ) {
				BlockInfo blockInfo = this.getInfo( data.getServerLevel(), data.player, data.position.offset( x, 0, z ), data.hand, data.face );
				if( blockInfo != null ) {
					blockInfo.accept();
					data.itemStack.hurtAndBreak( 1, data.player, entity->entity.broadcastBreakEvent( data.hand ) );
				}
			}
		}
	}

	private BlockInfo getInfo( ServerLevel level, Player player, BlockPos position, InteractionHand hand, Direction direction ) {
		var pair = HoeItem.TILLABLES.get( level.getBlockState( position ).getBlock() );
		if( pair == null )
			return null;

		BlockHitResult hitResult = new BlockHitResult( player.position(), direction, position, true );
		UseOnContext context = new UseOnContext( player, hand, hitResult );
		Predicate< UseOnContext > predicate = pair.getFirst();
		Consumer< UseOnContext > consumer = pair.getSecond();

		return predicate.test( context ) ? new BlockInfo( predicate, consumer, context ) : null;
	}

	private record BlockInfo( Predicate< UseOnContext > predicate, Consumer< UseOnContext > consumer, UseOnContext context ) {
		public void accept() {
			this.consumer.accept( this.context );
		}
	}
}
