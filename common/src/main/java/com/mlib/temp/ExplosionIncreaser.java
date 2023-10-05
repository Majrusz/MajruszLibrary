package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnExplosion;
import com.mlib.contexts.base.Condition;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;

@AutoInstance
public class ExplosionIncreaser {
	public ExplosionIncreaser() {
		OnExplosion.listen( this::changeBehaviour )
			.addCondition( Condition.predicate( data->data.entity instanceof Player ) );

		OnExplosion.listen( OnExplosion::cancelExplosion )
			.addCondition( Condition.predicate( data->data.entity instanceof Ghast ) );
	}

	private void changeBehaviour( OnExplosion data ) {
		data.radius *= 10;
		data.skipBlockIf( blockPos->true );
	}
}
