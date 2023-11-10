package com.mlib.contexts;

import com.mlib.annotation.AutoInstance;
import com.mlib.entity.AttributeHandler;
import com.mlib.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

@AutoInstance
public class OnEntitySwimSpeedMultiplierGetForge {
	private final AttributeHandler attribute = new AttributeHandler( "majrusz_library_swim_speed", ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.MULTIPLY_TOTAL );

	public OnEntitySwimSpeedMultiplierGetForge() {
		OnEntityTicked.listen( data->{
			float multiplier = data.entity instanceof IMixinLivingEntity entity ? entity.mlib$getSwimSpeedMultiplier() : 1.0f;

			this.attribute.setValue( multiplier - 1.0f ).apply( data.entity );
		} );
	}
}
