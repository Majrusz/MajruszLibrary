package com.mlib.contexts;

import com.mlib.annotation.AutoInstance;
import com.mlib.entity.AttributeHandler;
import com.mlib.mixininterfaces.IMixinLivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

@AutoInstance
public class OnEntitySwimSpeedMultiplierGetNeoForge {
	private final AttributeHandler attribute = new AttributeHandler( "majrusz_library_swim_speed", ForgeMod.SWIM_SPEED::get, AttributeModifier.Operation.MULTIPLY_TOTAL );

	public OnEntitySwimSpeedMultiplierGetNeoForge() {
		OnEntityTicked.listen( data->{
			this.attribute.setValue( data.entity instanceof IMixinLivingEntity entity ? entity.getSwimSpeedMultiplier() : 0.0f ).apply( data.entity );
		} );
	}
}
