package com.mlib.temp;

import com.mlib.annotations.AutoInstance;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.contexts.OnItemAttributeTooltip;
import com.mlib.contexts.base.Condition;
import com.mlib.text.TextHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

@AutoInstance
public class StickDamageIncreaser {
	public StickDamageIncreaser() {
		OnEntityPreDamaged.listen( this::increaseDamage )
			.addCondition( Condition.predicate( OnEntityPreDamaged::isDirect ) )
			.addCondition( Condition.predicate( data->data.attacker != null ) )
			.addCondition( Condition.predicate( data->data.attacker.getMainHandItem().is( Items.STICK ) ) );

		OnItemAttributeTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.itemStack.is( Items.STICK ) ) );
	}

	private void increaseDamage( OnEntityPreDamaged data ) {
		data.extraDamage += 5.0f;
		data.spawnMagicParticles = true;
	}

	private void addTooltip( OnItemAttributeTooltip data ) {
		data.add( EquipmentSlot.MAINHAND, TextHelper.literal( "SUPER DUPER +%d ATTACK DAMAGE".formatted( 5 ) ) );
	}
}
