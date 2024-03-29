package com.majruszlibrary.events;

import com.majruszlibrary.annotation.AutoInstance;
import com.majruszlibrary.entity.AttributeHandler;
import com.majruszlibrary.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

@AutoInstance
public class OnEntitySwimSpeedMultiplierGetForge {
	private final AttributeHandler attribute = new AttributeHandler( "majrusz_library_swim_speed", ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.MULTIPLY_TOTAL );

	public OnEntitySwimSpeedMultiplierGetForge() {
		OnEntityTicked.listen( data->{
			float multiplier = data.entity instanceof IMixinLivingEntity entity ? entity.majruszlibrary$getSwimSpeedMultiplier() : 1.0f;

			this.attribute.setValue( multiplier - 1.0f ).apply( data.entity );
		} );
	}
}
