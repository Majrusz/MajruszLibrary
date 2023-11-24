package com.majruszlibrary.mixin;

import com.majruszlibrary.events.OnRaidDefeated;
import com.majruszlibrary.events.base.Events;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;

@Mixin( Raid.class )
public abstract class MixinRaid {
	private @Shadow Set< UUID > heroesOfTheVillage;

	@Inject(
		at = @At(
			opcode = 181, // putfield
			ordinal = 1,
			target = "Lnet/minecraft/world/entity/raid/Raid;status:Lnet/minecraft/world/entity/raid/Raid$RaidStatus;",
			value = "FIELD"
		),
		method = "tick ()V"
	)
	private void tick( CallbackInfo callback ) {
		Events.dispatch( new OnRaidDefeated( ( Raid )( Object )this, this.heroesOfTheVillage ) );
	}
}
