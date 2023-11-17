package com.mlib.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class ItemFabric implements IItemPlatform {
	@Override
	public < Type extends EntityType< ? extends Mob > > Supplier< SpawnEggItem > createEgg( Supplier< Type > type, int backgroundColor, int highlightColor ) {
		return ()->{
			SpawnEggItem item = new SpawnEggItem( type.get(), backgroundColor, highlightColor, new Item.Properties() );
			DispenserBlock.registerBehavior( item, ( source, stack )->{
				Direction face = source.getBlockState().getValue( DispenserBlock.FACING );
				try {
					type.get().spawn( source.getLevel(), stack, null, source.getPos().relative( face ), MobSpawnType.DISPENSER, face != Direction.UP, false );
				} catch( Exception exception ) {
					return ItemStack.EMPTY;
				}

				stack.shrink( 1 );
				source.getLevel().gameEvent( GameEvent.ENTITY_PLACE, source.getPos(), GameEvent.Context.of( source.getBlockState() ) );
				return stack;
			} );

			return item;
		};
	}
}
