package com.mlib.temp;

import com.mlib.MajruszLibrary;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnExpOrbPickedUp;
import com.mlib.contexts.base.Condition;
import com.mlib.data.SerializableStructure;

@AutoInstance
public class ExtraExperience {
	int multiplier = 10;

	public ExtraExperience() {
		OnExpOrbPickedUp.listen( data->data.value *= this.multiplier )
			.addCondition( Condition.predicate( data->data.player.isCrouching() ) );

		MajruszLibrary.CONFIG.defineInteger( "experience_multiplier", ()->this.multiplier, x->this.multiplier = x );
	}
}
