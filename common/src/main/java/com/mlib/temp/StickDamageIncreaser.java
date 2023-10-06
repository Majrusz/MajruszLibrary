package com.mlib.temp;

import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.contexts.OnItemAttributeTooltip;
import com.mlib.contexts.OnItemTooltip;
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

		OnItemTooltip.listen( this::addTooltip )
			.addCondition( Condition.predicate( data->data.itemStack.is( Items.STICK ) ) )
			.addCondition( Condition.predicate( OnItemTooltip::isAdvanced ) );
	}

	private void increaseDamage( OnEntityPreDamaged data ) {
		data.extraDamage += 5.0f;
		data.spawnMagicParticles = true;
	}

	private void addTooltip( OnItemAttributeTooltip data ) {
		data.add( EquipmentSlot.MAINHAND, TextHelper.literal( "SUPER DUPER +%d ATTACK DAMAGE".formatted( 5 ) ) );
	}

	private void addTooltip( OnItemTooltip data ) {
		data.components.add( TextHelper.literal( "THE ULTIMATE WEAPON" ) );
	}
}
