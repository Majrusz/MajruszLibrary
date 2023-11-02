package com.mlib.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( CriteriaTriggers.class )
public interface IMixinCriteriaTriggers {
	@Invoker( "register" )
	static < Type extends CriterionTrigger< ? > > Type register( String id, Type trigger ) {
		throw new AssertionError();
	}
}
