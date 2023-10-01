package com.mlib.mixin;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( BreedGoal.class )
public interface IMixinBreedGoal {
	@Accessor( "animal" )
	Animal getAnimal();

	@Accessor( "partner" )
	Animal getPartner();
}
