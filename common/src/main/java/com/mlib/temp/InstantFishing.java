package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnFishingTimeGet;
import com.mlib.contexts.base.Condition;
import net.minecraft.world.item.Items;

@AutoInstance
public class InstantFishing {
	public InstantFishing() {
		OnFishingTimeGet.listen( data->data.value = 1 )
			.addCondition( Condition.predicate( data->data.player != null ) )
			.addCondition( Condition.predicate( data->data.player.getOffhandItem().is( Items.APPLE ) ) );
	}
}
