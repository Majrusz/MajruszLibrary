package com.mlib.features;

import com.mlib.annotations.AutoInstance;
import com.mlib.events.FarmlandTillCheckEvent;
import com.mlib.gamemodifiers.Condition;
import com.mlib.gamemodifiers.GameModifier;
import com.mlib.gamemodifiers.contexts.OnPlayerInteract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@AutoInstance
public class FarmlandTiller extends GameModifier {
	final Function< OnPlayerInteract.Data, Player > PLAYER_SUPPLIER = data->data.player;

	public FarmlandTiller() {
		OnPlayerInteract.Context onInteract = new OnPlayerInteract.Context( this::applyHoeBonus );
		onInteract.addCondition( new Condition.IsServer<>() )
			.addCondition( new Condition.IsShiftKeyDown<>( PLAYER_SUPPLIER ).negate() )
			.addCondition( OnPlayerInteract.IS_BLOCK_INTERACTION )
			.addCondition( OnPlayerInteract.HAS_FACE );

		this.addContext( onInteract );
	}

	private void applyHoeBonus( OnPlayerInteract.Data data ) {
		FarmlandTillCheckEvent event = this.dispatchEvent( data );
		if( event == null || this.getInfo( data.level, data.player, data.position, data.hand, data.face ) == null )
			return;

		for( int x = -event.area; x <= event.area; ++x ) {
			for( int z = -event.area; z <= event.area; ++z ) {
				BlockInfo blockInfo = this.getInfo( data.level, data.player, data.position.offset( x, 0, z ), data.hand, data.face );
				if( blockInfo != null ) {
					blockInfo.accept();
					data.itemStack.hurtAndBreak( 1, data.player, entity->entity.broadcastBreakEvent( data.hand ) );
				}
			}
		}
	}

	@Nullable
	private FarmlandTillCheckEvent dispatchEvent( OnPlayerInteract.Data data ) {
		FarmlandTillCheckEvent event = new FarmlandTillCheckEvent( data.level, data.player, data.itemStack );

		return !MinecraftForge.EVENT_BUS.post( event ) && event.area > 0 ? event : null;
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
