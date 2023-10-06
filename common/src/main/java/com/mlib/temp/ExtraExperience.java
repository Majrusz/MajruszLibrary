package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnExpOrbPickedUp;
import com.mlib.contexts.base.Condition;

@AutoInstance
public class ExtraExperience {
	public ExtraExperience() {
		OnExpOrbPickedUp.listen( data->data.value *= 10 )
			.addCondition( Condition.predicate( data->data.player.isCrouching() ) );
	}
}
