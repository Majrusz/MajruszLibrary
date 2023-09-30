package net.mlib.temp;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mlib.annotations.AutoInstance;
import net.mlib.contexts.OnLootGenerated;
import net.mlib.contexts.base.Condition;
import net.mlib.items.ItemHelper;

import java.util.Optional;

@AutoInstance
public class Smelter {
	public Smelter() {
		OnLootGenerated.listen( this::smelt )
			.addCondition( OnLootGenerated.hasBlockState() )
			.addCondition( OnLootGenerated.hasTool() )
			.addCondition( Condition.hasAuthority() )
			.addCondition( Condition.hasLevel() )
			.addCondition( Condition.predicate( data->data.entity instanceof Player player && !player.isCrouching() ) );
	}

	private void smelt( OnLootGenerated.Data data ) {
		ObjectArrayList< ItemStack > smeltedLoot = new ObjectArrayList<>();
		float experience = 0.0f;
		for( ItemStack itemStack : data.generatedLoot ) {
			Optional< ItemHelper.SmeltResult > result = ItemHelper.tryToSmelt( data.getLevel(), itemStack );
			if( result.isPresent() ) {
				smeltedLoot.add( result.get().itemStack() );
				experience += result.get().experience();
			} else {
				smeltedLoot.add( itemStack );
			}
		}

		data.generatedLoot.clear();
		data.generatedLoot.addAll( smeltedLoot );
	}
}
