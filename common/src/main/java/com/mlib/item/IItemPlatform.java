package com.mlib.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public interface IItemPlatform {
	< Type extends EntityType< ? extends Mob > > Supplier< SpawnEggItem > createEgg( Supplier< Type > type, int backgroundColor, int highlightColor );
}
