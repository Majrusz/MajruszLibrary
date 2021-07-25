package com.mlib.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/** Class responsible for simple adding new spawn eggs to entities with all standard minecraft behavior. */
public class SpawnEggFactory {
	private static final SpawnEggBehavior SPAWN_EGG_BEHAVIOR = new SpawnEggBehavior();
	private static CreativeModeTab ITEM_GROUP = CreativeModeTab.TAB_MISC;

	/**
	 Adds dispense behavior to given spawn egg item. (entity will be spawned from using dispenser)

	 @param spawnEgg Spawn egg to add new behavior.
	 */
	public static void addDispenseBehaviorTo( SpawnEggItem spawnEgg ) {
		DispenserBlock.registerBehavior( spawnEgg, SPAWN_EGG_BEHAVIOR );
	}

	/**
	 Creates spawn egg item and registers all standard minecraft behavior.
	 (does not register that item, to register you need to use createRegistrySpawnEgg)

	 @param entityType     Entity type spawn egg should spawn.
	 @param primaryColor   Primary color used by the egg. (spots on the egg)
	 @param secondaryColor Secondary color used by the egg. (egg background)
	 */
	public static SpawnEggItem createSpawnEgg( EntityType< ? extends Mob > entityType, int primaryColor, int secondaryColor ) {
		Item.Properties itemProperties = new Item.Properties().tab( ITEM_GROUP );
		SpawnEggItem spawnEggItem = new SpawnEggItem( entityType, primaryColor, secondaryColor, itemProperties );
		addDispenseBehaviorTo( spawnEggItem );

		return spawnEggItem;
	}

	/**
	 Creates and registers spawn egg item.

	 @param deferredRegister Deferred register to which the spawn egg will be registered.
	 @param registryName     Registry name of the egg.
	 @param entityType       Entity type spawn egg should spawn.
	 @param primaryColor     Primary color used by the egg. (spots on the egg)
	 @param secondaryColor   Secondary color used by the egg. (egg background)
	 */
	public static RegistryObject< SpawnEggItem > createRegistrySpawnEgg( DeferredRegister< Item > deferredRegister, String registryName,
		EntityType< ? extends Mob > entityType, int primaryColor, int secondaryColor
	) {
		return deferredRegister.register( registryName, ()->createSpawnEgg( entityType, primaryColor, secondaryColor ) );
	}

	/**
	 Creates and registers spawn egg item.

	 @param deferredRegister Deferred register to which the spawn egg will be registered.
	 @param registryName     Registry name of the egg.
	 @param spawnEgg         Pre-created spawn egg item.
	 */
	public static RegistryObject< SpawnEggItem > createRegistrySpawnEgg( DeferredRegister< Item > deferredRegister, String registryName,
		SpawnEggItem spawnEgg
	) {
		return deferredRegister.register( registryName, ()->spawnEgg );
	}

	/**
	 Changes default item group for spawn eggs.
	 Previously created eggs before calling this function will not be affected!

	 @param modeTab New item group to set.
	 */
	public static void setDefaultItemGroup( CreativeModeTab modeTab ) {
		ITEM_GROUP = modeTab;
	}

	private static final class SpawnEggBehavior extends DefaultDispenseItemBehavior {
		@Override
		public ItemStack execute( BlockSource source, ItemStack stack ) {
			BlockPos blockPosition = source.getPos();
			BlockState blockState = source.getBlockState();
			Direction direction = blockState.getValue( DispenserBlock.FACING );
			EntityType< ? > entityType = ( ( SpawnEggItem )stack.getItem() ).getType( stack.getTag() );
			entityType.spawn( source.getLevel(), stack, null, blockPosition.offset( direction.getNormal() ), MobSpawnType.DISPENSER,
				direction != Direction.UP, false
			);
			stack.shrink( 1 );

			return stack;
		}
	}
}
