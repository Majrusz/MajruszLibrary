package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnEnderManAngered;
import com.mlib.contexts.base.Condition;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

@AutoInstance
public class EnderManLeatherProtector {
	public EnderManLeatherProtector() {
		OnEnderManAngered.listen( OnEnderManAngered::cancelAnger )
			.addCondition( Condition.predicate( data->data.player.getItemBySlot( EquipmentSlot.HEAD ).is( Items.TURTLE_HELMET ) ) );
	}
}
