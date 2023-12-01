package com.majruszlibrary.mixin;

import com.majruszlibrary.mixininterfaces.IMixinMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Mob.class )
public abstract class MixinMob implements IMixinMob {
	private MobSpawnType majruszlibrary$mobSpawnType = null;

	@Inject(
		at = @At( "HEAD" ),
		method = "finalizeSpawn (Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;"
	)
	private void finalizeSpawn( ServerLevelAccessor $$0, DifficultyInstance $$1, MobSpawnType mobSpawnType, @Nullable SpawnGroupData $$3,
		@Nullable CompoundTag $$4, CallbackInfoReturnable< SpawnGroupData > callback
	) {
		this.majruszlibrary$mobSpawnType = mobSpawnType;
	}

	@Override
	public MobSpawnType getMobSpawnType() {
		return this.majruszlibrary$mobSpawnType;
	}
}
