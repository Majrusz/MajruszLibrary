package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.contexts.base.Condition;
import net.minecraft.world.item.Items;

@AutoInstance
public class StickDamageIncreaser {
	public StickDamageIncreaser() {
		OnEntityPreDamaged.listen( this::increaseDamage )
			.addCondition( Condition.predicate( OnEntityPreDamaged::isDirect ) )
			.addCondition( Condition.predicate( data->data.attacker != null ) )
			.addCondition( Condition.predicate( data->data.attacker.getMainHandItem().is( Items.STICK ) ) );
	}

	private void increaseDamage( OnEntityPreDamaged data ) {
		data.extraDamage += 5.0f;
		data.spawnMagicParticles = true;
	}
}
