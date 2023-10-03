package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnEntityEffectCheck;
import com.mlib.contexts.base.Condition;
import net.minecraft.world.entity.animal.Fox;

@AutoInstance
public class FoxEffectImmunity {
	public FoxEffectImmunity() {
		OnEntityEffectCheck.listen( OnEntityEffectCheck::cancelEffect )
			.addCondition( Condition.predicate( data->data.entity instanceof Fox ) );
	}
}
